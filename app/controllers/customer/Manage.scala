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

  private [this] val loggingAction = actionUtils.loggingAction

  def setPassword = loggingAction.async { implicit request =>
    Future.successful(Ok(views.html.account.setPassword("设置密码")))
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
                userDao.addUser(email, "", 0, "", "",secure,name,cur).map{
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

  def resetPasswordType() = loggingAction.async { implicit request =>
    Future.successful(Ok(views.html.account.forgetPassword("找回密码",3)))
  }

}
