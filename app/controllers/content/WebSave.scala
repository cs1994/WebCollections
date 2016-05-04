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
import scala.concurrent.{Await, Future}
import controllers.{ActionUtils,SessionKey}
import models.dao.{WebSaveDao,UserDao}
import util.SecureUtil._
import common.errorcode.{CustomerErrorCode}

@Singleton
class WebSave @Inject() (webSaveDao:WebSaveDao,
                        userDao:UserDao,
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
        val result= webSaveDao.addSaveFile(url,description,label,secret,userId,cur)
          result._1.map{id=>
          if(id>0){
            Ok(successResult(Json.obj("id" ->id,"content" -> result._2,"time"->cur)))
          }else{
            Ok(CustomerErrorCode.addSaveFail)
          }
        }
      }
    }
  }

  def getPersonalSaveById =  customerAuth.async{implicit request=>
    val userId=request.session.get(SessionKey.userId).get.toLong
    webSaveDao.getPersonalSave(userId).flatMap { saveList =>
      if(saveList.isEmpty){
        Future.successful(Ok(CustomerErrorCode.saveListEmpty))
      }else{
       val result = saveList.map{l=>
         val commentList = webSaveDao.getCommentById(l._1.id).map{
           comments=>
            comments.map{comment=>
               val userInfo = Await.result(userDao.getUserById(comment.fromId).map{info=>
                 Json.obj(
                   "id" -> info.id,
                   "headImg" -> info.headImg,
                   "nickName" -> info.nickName
                 )
               },Duration(3, concurrent.duration.SECONDS))
                 Json.obj(
                   "userInfo" -> userInfo,
                   "content" -> comment.content
                 )
             }
         }
         commentList.map{com=>
           Json.obj(
               "id" -> l._1.id,
               "url" -> l._1.url,
               "des" -> l._1.description,
               "secret" -> l._1.secret,
               "number" -> l._1.number,
               "content" -> l._1.webcontent,
               "insertTime" -> l._1.insertTime,
               "commentNum" -> l._1.commentNum,
               "flag" -> l._1.flag,
               "label" -> l._2.labelNum,
               "commentList" ->com
             )
         }
       }
        Future.sequence(result).map { res =>
          Ok(successResult(Json.obj("result" ->res)))
        }
      }
    }
  }

  def deletePersonalSave(id:Long) = customerAuth.async{implicit request=>
    val userId=request.session.get(SessionKey.userId).get.toLong
    webSaveDao.deletePersonalSave(id,userId).map { res =>
      if(res>0){
        Ok(success)
      }else{
       Ok(CustomerErrorCode.deleteSaveFail)
      }
    }
  }


  def getAllSave =  customerAuth.async{implicit request=>
//    val userId=request.session.get(SessionKey.userId).get.toLong
    webSaveDao.getAllSaveList.flatMap { saveList =>
      if(saveList.isEmpty){
        Future.successful(Ok(CustomerErrorCode.saveListEmpty))
      }else{
        val result = saveList.map{l=>
          val commentList = webSaveDao.getCommentById(l._1.id).map{
            comments=>
              comments.map{comment=>
                val userInfo = Await.result(userDao.getUserById(comment.fromId).map{info=>
                  Json.obj(
                    "id" -> info.id,
                    "headImg" -> info.headImg,
                    "nickName" -> info.nickName
                  )
                },Duration(3, concurrent.duration.SECONDS))
                Json.obj(
                  "userInfo" -> userInfo,
                  "content" -> comment.content
                )
              }
          }
          commentList.map{com=>
            val nickName =Await.result( userDao.findById(l._1.userId).map(u=>u.get.nickName),Duration(3, concurrent.duration.SECONDS))
            Json.obj(
              "id" -> l._1.id,
              "userId" -> l._1.userId,
              "nickName" -> nickName,
              "url" -> l._1.url,
              "des" -> l._1.description,
              "secret" -> l._1.secret,
              "number" -> l._1.number,
              "content" -> l._1.webcontent,
              "insertTime" -> l._1.insertTime,
              "commentNum" -> l._1.commentNum,
              "flag" -> l._1.flag,
              "label" -> l._2.labelNum,
              "commentList" ->com
            )
          }
        }
        Future.sequence(result).map { res =>
          Ok(successResult(Json.obj("result" ->res)))
        }
      }
    }
  }

  def addComment = customerAuth.async{implicit request=>

    val userId=request.session.get(SessionKey.userId).get.toLong
    val postData=request.body.asJson.get
    val saveId=(postData \ "saveId").as[Long]
    val toId=(postData \ "toId").as[Long]
    val content=(postData \ "content").as[String]
    webSaveDao.addComment(userId,saveId,toId,content).map { res =>
      if(res>0){
        userDao.addUserCommentNumber(userId)
        webSaveDao.addSaveCommentNumber(saveId)
        Ok(successResult(Json.obj("id" ->res)))
      }else{
        Ok(CustomerErrorCode.addCommentFail)
      }
    }
  }
  def personalComment = customerAuth.async{implicit request=>

    val userId=request.session.get(SessionKey.userId).get.toLong

    webSaveDao.personalComment(userId).map { res =>
//      if(res.nonEmpty){
        val result = res.map{r=>
        Json.obj(
        "id" -> r._1.id,
        "content" -> r._1.content,
        "state" -> r._1.state,
        "fromId" -> r._1.fromId,
        "nickName" -> r._2.nickName
        )
        }
        Ok(successResult(Json.obj("result" ->result)))
//      }else{
//        Ok(CustomerErrorCode.deleteSaveFail)
//      }
    }
  }

  def deleteCommentById(id:Long)=customerAuth.async{implicit request=>

    val userId=request.session.get(SessionKey.userId).get.toLong

    webSaveDao.deleteCommentById(userId,id).map { res =>
            if(res>0){
              Ok(successResult(Json.obj("id" ->res)))
            }else{
              Ok(CustomerErrorCode.deleteSaveFail)
            }
    }
  }
}
