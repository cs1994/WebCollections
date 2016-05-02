package controllers.content


import javax.inject.Inject
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
import models.dao.{WebSaveDao}
import util.SecureUtil._
import common.errorcode.{CustomerErrorCode}

@Singleton
class WebSave @Inject() (webSaveDao:WebSaveDao,
                        val actionUtils: ActionUtils
                         ) extends Controller with JsonProtocols {
  import actionUtils._
  private [this] val loggingAction = actionUtils.loggingAction
  private [this] val customerAuth = loggingAction andThen customerAction

  def addSave = customerAuth.async{implicit request=>
    val postData=request.body.asJson.get
    val url=(postData \ "url").as[String]
    val description=(postData \ "description").as[String]
    val label=(postData \ "label").as[Int]
    val secret=(postData \ "secret").as[Int]
    val userId=request.session.get(SessionKey.userId).get.toLong
    webSaveDao.getSaveByUrl(url,userId).flatMap { urlOpt =>
      if(urlOpt.isDefined){
        Future.successful(Ok(CustomerErrorCode.urlExist))
      }else{
        val cur = System.currentTimeMillis()
        webSaveDao.addSave(
          url,description,label,secret,userId,cur
        ).map{result=>
          if(result>0){
            Ok(successResult(Json.obj("id" ->result)))
          }else{
            Ok(CustomerErrorCode.addSaveFail)
          }
        }
      }
    }
  }

}
