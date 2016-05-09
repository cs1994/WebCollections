package controllers.customer


import java.io.File
import javax.inject.Inject
import com.github.nscala_time.time.Imports._
import org.apache.commons.codec.digest.DigestUtils
import util.{SecureUtil, ValidateUtil, GenCodeUtil}
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
import common.AppSettings


@Singleton
class Manage @Inject() (emailValidateDao:EmailValidateDao,
                       userDao: UserDao,
                        appSettings:AppSettings,
                         val actionUtils: ActionUtils
                          ) extends Controller with JsonProtocols {
  import actionUtils._
  private final val IMAGE_SAVE_PREFIX  = appSettings.imageSavePrefix
  private final val IMAGE_ACCESS_PREFIX  = appSettings.imageAccessPrefix
  private [this] val loggingAction = actionUtils.loggingAction
  private [this] val customerAuth = loggingAction andThen customerAction
  private def genUniqueName(fileName:String) = {
    SecureUtil.nonceStr(16)+DigestUtils.md5Hex(SecureUtil.nonceStr(32)+DateTime.now.getMillis.toString+fileName)
  }


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
                      userDao.addDir(uid)
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

  def uploadUserPic = Action.async{ request=>
    /**
     * upload user's head images
     * saveDir = /public/headPic
     */
    request.body.asMultipartFormData match {
      case Some(mutilForm)=>{
        if(mutilForm.file("imgFile").isDefined){
          //todo
          val dirPath = s"/"
          val dir = new File(IMAGE_SAVE_PREFIX+dirPath)
          if(!dir.exists()){
            dir.mkdirs()
          }
          val temp = mutilForm.file("imgFile").head
          val postFix = temp.filename.split("\\.",2).last
          val fileName = (dirPath+genUniqueName(temp.filename)+ "."+postFix).replaceAll(" ","")
          try {
            temp.ref.moveTo(new File(IMAGE_SAVE_PREFIX+fileName))
            val accessUrl = IMAGE_ACCESS_PREFIX+fileName
            Future.successful(Ok(successResult(Json.obj("url"->accessUrl))))
          }catch {
            case e:Exception =>
//              logger.info("upload pic failed:",e)
              Future.successful(Ok(CustomerErrorCode.uploadImageFail))
          }
        }else{
          Future.successful(Ok(CustomerErrorCode.invalidForm))
        }
      }
      case None=>{
        Future.successful(Ok(CustomerErrorCode.invalidForm))
      }
    }
  }
  def modifyUserImg(headImg:String) = customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong

    userDao.modifyUserImg(userId,headImg).map { res =>
      if(res > 0)
        Ok(successResult(Json.obj("data" -> res)))
      else
        Ok(jsonResult(10000,"修改个人信息失败"))
    }
  }

  def editUserInfo = customerAuth.async {implicit request =>
    val postData=request.body.asJson.get
    val phone=(postData \ "phone").as[String]
    val birthday=(postData \ "birthday").as[String]
    val gen=(postData \ "gen").as[Int]
    val nickName=(postData \ "nickName").as[String]
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.modifyUserInfo(userId,phone,birthday,gen,nickName).map { res =>
      if(res > 0)
        Ok(successResult(Json.obj("data" -> res)))
      else
        Ok(jsonResult(10000,"修改个人信息失败"))
    }
  }
  def getUserInfoById = customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.findById(userId).map { res =>
      if(res.isDefined)
        Ok(successResult(Json.obj("info" -> res.map(rUserWriter.writes))))
      else
        Ok(jsonResult(10000,"修改个人信息失败"))
    }
  }

  def addNewTask = customerAuth.async {implicit request =>
    val postData=request.body.asJson.get
    val content=(postData \ "content").as[String]
    val state=(postData \ "state").as[Int]
    val userId=request.session.get(SessionKey.userId).get.toLong
    val cur = System.currentTimeMillis()
    userDao.addNewTask(userId,content,state,cur).map { res =>
      if(res>0)
        Ok(successResult(Json.obj("id" -> res,"insertTime"->cur)))
      else
        Ok(jsonResult(10000,"添加失败"))
    }
  }
  def getAllTask = customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.getAllTask(userId).map { res =>
      if(res.nonEmpty){
        Ok(successResult(Json.obj("list"->res.map(rTaskWriter.writes))))
      }
      else
        Ok(jsonResult(10000,"添加失败"))
    }
  }
  def changeTaskState(id:Long,state:Int) = customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.changeTaskState(userId,id,state).map { res =>
      if(res>0){
        Ok(success)
      }
      else
        Ok(jsonResult(10000,"更新失败"))
    }
  }
  def deleteTask(id:Long) = customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.deleteTask(userId,id).map { res =>
      if(res>0){
        Ok(success)
      }
      else
        Ok(jsonResult(10000,"删除失败"))
    }
  }

  def getTaskBtState(state:Int) = customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.getTaskByState(userId,state).map { res =>
//      if(res.nonEmpty){
        Ok(successResult(Json.obj("list"->res.map(rTaskWriter.writes))))
//      }
//      else
//        Ok(jsonResult(10000,"获取失败"))
    }
  }
  def getUnfinishedTask =  customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.getUnfinishedTask(userId).map { res =>
      Ok(successResult(Json.obj("list"->res.map(rTaskWriter.writes))))
    }
  }
  def getUnstartTask =  customerAuth.async {implicit request =>
    val userId=request.session.get(SessionKey.userId).get.toLong
    userDao.getUnstartTask(userId).map { res =>
      Ok(successResult(Json.obj("list"->res.map(rTaskWriter.writes))))
    }
  }
}
