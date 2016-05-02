package controllers.customer


import javax.inject.Inject
import util.ValidateUtil
import util.GenCodeUtil
import common.errorcode.CustomerErrorCode
import com.google.inject.Singleton
import scala.concurrent.ExecutionContext.Implicits.global
import models.protocols.JsonProtocols
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.duration._
import scala.concurrent.Future
import controllers.{ActionUtils,SessionKey}
import models.dao.{EmailValidateDao,UserDao}
import util.SecureUtil._

@Singleton
class Manage @Inject() (emailValidateDao:EmailValidateDao,
                       userDao: UserDao,
                         val actionUtils: ActionUtils
                          ) extends Controller with JsonProtocols {
  import actionUtils._

  private [this] val loggingAction = actionUtils.loggingAction
  private [this] val customerAuth = loggingAction andThen customerAction

  def setPassword = loggingAction.async { implicit request =>
    Future.successful(Ok(views.html.account.setPassword("设置密码",None)))
  }

  def tokenRegisterForm = Form(
    tuple(
      "token" -> nonEmptyText,
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  def registerWithEmail = loggingAction.async { implicit request =>
    val cur = System.currentTimeMillis()
    tokenRegisterForm.bindFromRequest.value match {
      case Some((token,name,password)) =>{
        emailValidateDao.getByToken(token).flatMap{ validateOpt=>
          if(validateOpt.isDefined && validateOpt.get.expiredTime>=cur && validateOpt.get.validate == 1){
            val email = validateOpt.get.email
            userDao.getUserByEmail(email).flatMap{ customerOpt=>
              if(customerOpt.isEmpty){
                val ip = request.remoteAddress
                val secure = getSecurePassword(password, ip, cur)
                userDao.addUser(email, "", 0, "", "",secure,name,cur,ip).map{
                  uid=>
                    if(uid > 0) {
//                      val timestamp = System.currentTimeMillis().toString
                      Ok(success).withSession(
                        SessionKey.userId -> uid.toString,
                        SessionKey.email -> email,
                        SessionKey.nickName -> name
                      )
                    }else{
                      Ok(CustomerErrorCode.registerFail)
                    }
                }
              }else{
                Future.successful(Ok(CustomerErrorCode.emailAlreadyExisted))
              }
            }
          }else{
            Future.successful(Ok(CustomerErrorCode.invalidEmailToken))
          }
        }
      }
      case None =>{
        Future.successful(Ok(CustomerErrorCode.missingParameters))
      }
    }
  }

  def resetPasswordType = loggingAction.async { implicit request =>
    Future.successful(Ok(views.html.account.forgetPassword("找回密码",3,None)))
  }

  def tokenPwdForm = Form(
    tuple(
      "token" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )
  def resetPwdByEmail = loggingAction.async{implicit request=>
    val cur = System.currentTimeMillis()
    tokenPwdForm.bindFromRequest.value match {
      case Some((token,password)) =>{
        emailValidateDao.getByToken(token).flatMap{ validateOpt=>
          if(validateOpt.isDefined && validateOpt.get.expiredTime>=cur && validateOpt.get.validate==1){
            val email = validateOpt.get.email
            userDao.getUserByEmail(email).flatMap{userOpt=>
              if(userOpt.isEmpty){
                Future.successful(Ok(CustomerErrorCode.userNotExist))
              }else{
                userDao.updatePwdByEmail(userOpt.get,password).map{rows=>
                  if(rows>0){
                    Ok(success)
                  }else{
                    Ok(CustomerErrorCode.updatePwdFail)
                  }
                }
              }
            }
          }else{
            Future.successful(Ok(CustomerErrorCode.invalidToken))
          }
        }
      }
      case None =>{
        Future.successful(Ok(CustomerErrorCode.missingParameters))
      }
    }
  }

  def checkPwdForm = Form(
    "oldpwd" -> nonEmptyText
  )

  def changePwdForm = Form(
    "newpwd" -> nonEmptyText
  )
  def checkPwd = customerAuth.async{implicit request =>
    checkPwdForm.bindFromRequest.value match{
      case Some(oldPwd) =>
        val user = request.customer
        if(userDao.checkPassword(user,oldPwd)){
          Future.successful(Ok(success))
        }else{
          Future.successful(Ok(CustomerErrorCode.passwordError))
        }
      case None => Future.successful(Ok(CustomerErrorCode.missingParameters))
    }
  }

  def changePwd = customerAuth.async{implicit request =>
    changePwdForm.bindFromRequest.value match{
      case Some(newPwd) =>
        val user = request.customer
        userDao.changePwd(user,newPwd).map{res =>
          if(res > 0){
            Ok(success)
          }else{
            Ok(CustomerErrorCode.updatePwdFail)
          }
        }
      case None => Future.successful(Ok(CustomerErrorCode.missingParameters))
    }
  }
}
