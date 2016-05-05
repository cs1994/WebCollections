package models.dao

import javax.inject.Inject

import com.google.inject.Singleton
import models.tables.SlickTables
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.io.Source
import java.io._
import controllers.WebGet

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class WebSaveDao @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                         ) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val log = Logger(this.getClass)

  private[this] val uSave = SlickTables.tSave
  private[this] val uLabel = SlickTables.tLabel
  private[this] val uComment = SlickTables.tComment
  private[this] val uCustomer = SlickTables.tUser
  private[this] val uCommentLink = SlickTables.tCommentLink



  def getSaveByUrl(url:String,userId:Long) = {
    db.run(uSave.filter(s=>(s.url === url)&&(s.userId===userId)).result.headOption)
  }
  def getSaveById(id:Long) = {
    db.run(uSave.filter(_.id===id).result.head)
  }

  def insertSave(url:String,description:String,secret:Int,userId:Long,insertTime:Long,webContent:String,flag:Int)={
    db.run(uSave.map(t=>(t.url,t.description,t.secret,t.userId,t.insertTime,t.webcontent,t.number,t.commentNum,t.flag)).returning(
      uSave.map(_.id))+=(url,description,secret,userId,insertTime,webContent,0,0,flag)).mapTo[Long]
  }
  def addSaveFile(url:String,description:String,label:Int,secret:Int,userId:Long,insertTime:Long)={

    val content = WebGet.getUrlContent(url)
    val pattern = "[!-~]+".r
    val newContent = pattern.replaceAllIn(content, "").trim.replaceAll("  ", "")
    val flag = 1
    val id= insertSave(url,description,secret,userId,insertTime,newContent.slice(0,30),flag)
    id.map{saveId=>
      db.run(uLabel.map(t=>(t.taskId,t.labelNum,t.userId)).returning(
        uLabel.map(_.id))+=(saveId,label,userId)).mapTo[Long]
      WebGet.saveToFile("public/web" + "/" + saveId  + ".html",content)
      WebGet.saveToFile("public/source" + "/" + saveId  + ".txt",newContent)
  }
    (id,newContent.slice(0,30))
  }

//  def addSave(url:String,description:String,label:Int,secret:Int,userId:Long,insertTime:Long)={
//    try{
//      addSaveFile(url,description,label,secret,userId,insertTime)._1.map{r => }
//    }catch{
//      case e:Exception => Future.successful(0)
//    }
//  }

  def getPersonalSave(userId:Long) = {
    db.run{
      uSave.filter(_.userId===userId).sortBy(_.insertTime).join(uLabel).on(_.id===_.taskId).result}
  }

  def getCommentById(id:Long)={
    db.run{
      uComment.filter(_.saveId===id).result}
  }

  def deletePersonalSave(id:Long,userId:Long)={
    val htmlFile = new File("public/web/"+id+".html")
    val txtFile = new File("public/source/"+id+".txt")
    if (!htmlFile.exists() || !txtFile.exists()) {
      Future(0)
    }
    else{
      htmlFile.delete()
      txtFile.delete()
      db.run{uSave.filter(s=>(s.id===id)&&(s.userId === userId)).delete}
    }
  }

  def getAllSaveList={
    db.run{uSave.filter(_.secret===2).join(uLabel).on(_.id===_.taskId).result}
  }

  def addComment(fromId:Long,saveId:Long,toId:Long,content:String) = {
    db.run{uComment.map(c=>(c.fromId,c.saveId,c.toId,c.content,c.state,c.flag)).returning(
      uComment.map(_.id))+=(fromId,saveId,toId,content,0,0)}.mapTo[Long]
  }

  def addSaveCommentNumber(saveId:Long)={
    db.run(uSave.filter(_.id===saveId).map(_.commentNum).update(+1))
  }

  def personalComment(userId:Long)={
    db.run{uComment.filter(c=>(c.toId === userId)&&(c.flag===0)).join(uCustomer).on(_.fromId ===_.id).result}
  }
  def personalReplyComment(userId:Long)={
    db.run{uComment.filter(c=>(c.toId === userId)&&(c.flag===1)).join(uCustomer).on(_.fromId ===_.id).result}
  }
//  def getPersonalCommentUnsee(userId:Long)={
//    db.run{uComment.filter(c=>(c.toId === userId)&&(c.state==0)).join(uCustomer).on(_.fromId ===_.id).result}
//  }

  def deleteCommentById(userId:Long,id:Long)={
    db.run{uComment.filter(c=>(c.id === id)&&(c.toId==userId)).delete}
  }
  def findCommentLink(toId:Long) ={
    db.run{uCommentLink.filter(_.toId===toId).result.headOption}
  }

  def replyComment(userId:Long,toId:Long,content:String,saveId:Long)={
     db.run{uComment.map(c=>(c.fromId,c.saveId,c.toId,c.content,c.state,c.flag)).returning(
      uComment.map(_.id))+=(userId,saveId,toId,content,0,1)}.mapTo[Long]
  }
  def addCommentLink(fromId:Long,toId:Long)={
    db.run{uCommentLink.map(uc=>(uc.fromId,uc.toId)).returning(uCommentLink.map(_.id))+=
      (fromId,toId)}.mapTo[Long]
  }

}