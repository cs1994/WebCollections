package controllers

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
import common.email.Email
import models.dao.{EmailValidateDao,UserDao}
import controllers.{ActionUtils,SessionKey}


@Singleton
class Auth @Inject()(  emailFunc:Email,
                       userDao: UserDao,
                       emailValidateDao:EmailValidateDao,
                      val actionUtils: ActionUtils) extends Controller with JsonProtocols{

  import actionUtils._
  val customerAuth = loggingAction andThen customerAction

  val loginForm = Form(
    tuple(
      "account" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  val registerForm = Form(
    tuple(
      "token" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  val resetPwdForm = Form(
    tuple(
      "oldpwd" -> nonEmptyText,
      "newpwd" -> nonEmptyText
    )
  )
  def registerPage = Action {
    Ok(views.html.account.index("注册",None))
  }
  def forgetPassword = Action {
    Ok(views.html.account.forgetPassword("找回密码",1,None))
  }


 def sendRegisterEmail(email:String) = Action.async{
    if(ValidateUtil.isEmail(email)){
      val token = GenCodeUtil.get64Token()
      val curTimestamp = System.currentTimeMillis()
      val expiredTime = curTimestamp + 24*60*60*1000L
      emailFunc.SendRegisterEmailTask(token,email).flatMap{res =>
        if(res > 0){
          emailValidateDao.add(email,token,expiredTime,curTimestamp).map{
            result =>
              println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + result)
              if(result>0){
                Ok(success)
              }
              else {
                Ok(CustomerErrorCode.failInsert)
              }
          }
        }else{
          Future.successful(Ok(CustomerErrorCode.sendConfirmEmailFail))
        }
      }
    }else{
      Future.successful(Ok(CustomerErrorCode.invalidEmailFormat))
    }
  }
  def validateRegisterEmail(token:String) = Action.async{

    val curTimestamp = System.currentTimeMillis()

    emailValidateDao.updateStateByToken(token,1,curTimestamp).map{rows=>
      if(rows>0){
        Redirect(s"/register/setpassword?token=$token")
      }else{
        Ok(jsonResult(10000,"验证邮箱失败"))
      }
    }
  }

  def sendConfirmEmail(email:String) = Action.async{

    if(ValidateUtil.isEmail(email)){
      val token = GenCodeUtil.get64Token()
      val curTimestamp = System.currentTimeMillis()
      val expiredTime = curTimestamp + 24*60*60*1000L
      emailFunc.SendConfirmEmailTask(token,email).flatMap{res =>
        if(res>0){
          emailValidateDao.add(email,token,expiredTime,curTimestamp).map{
            result=>
              if(result>0 && emailFunc.sendConfirm){
                Ok(success)
              }else{
                Ok(CustomerErrorCode.sendConfirmEmailFail)
              }
          }
        }
        else
          Future.successful(Ok(CustomerErrorCode.sendConfirmEmailFail))
      }
      }
    else{
      Future.successful(Ok(CustomerErrorCode.invalidEmailFormat))
    }
  }
  def validateConfirmEmail(token:String) = Action.async{

    val curTimestamp = System.currentTimeMillis()

    emailValidateDao.updateStateByToken(token,1,curTimestamp).map{rows=>
      if(rows>0){
        Redirect(s"/customer/findPassword?token=$token")//TODO
      }else{
        Ok(jsonResult(10000,"验证邮箱失败"))
      }
    }
  }



  def checkEmailExist(email:String) = loggingAction.async{
    userDao.getUserByEmail(email).map{
      user =>
        if(user.isDefined){
          Ok(successResult(Json.obj("is_exists" -> true)))
        }else{
          Ok(successResult(Json.obj("is_exists" -> false)))
        }
    }
  }

  def loginSubmit = loggingAction.async{ implicit request =>
    loginForm.bindFromRequest.value match{
      case Some((account,pwd)) =>
        val userFuture = userDao.getUserByEmail(account)
        userFuture.map{userInfo =>
//          logger.debug(userInfo.isDefined.toString)
          if(userInfo.isDefined && userDao.checkPassword(userInfo.get,pwd)){
//            val timestamp = System.currentTimeMillis().toString
            val userId = userInfo.get.id
            Ok(success).withSession(
              SessionKey.userId -> userId.toString,
              SessionKey.email -> userInfo.get.email,
              SessionKey.headImg -> userInfo.get.headImg,
              SessionKey.sex -> userInfo.get.sex.toString,
              SessionKey.phone -> userInfo.get.phone,
              SessionKey.birthday -> userInfo.get.birthday,
              SessionKey.nickName -> userInfo.get.nickName
            )
          }else{
            Ok(jsonResult(10010, "user not exists or password error."))
          }
        }
      case None => Future.successful(Ok(jsonResult(10010, "Input format error.")))
    }
  }

  def logout = loggingAction {
    Redirect(routes.Application.login()).withNewSession
  }

}
