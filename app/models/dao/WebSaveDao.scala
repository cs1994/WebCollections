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


  def getSaveByUrl(url:String,userId:Long) = {
    db.run(uSave.filter(s=>(s.url === url)&&(s.userId===userId)).result.headOption)
  }

  def insertSave(url:String,description:String,secret:Int,userId:Long,insertTime:Long,webContent:String,flag:Int)={
    db.run(uSave.map(t=>(t.url,t.description,t.secret,t.userId,t.insertTime,t.webcontent,t.number,t.commentNum,t.flag)).returning(
      uSave.map(_.id))+=(url,description,secret,userId,insertTime,webContent,0,0,flag)).mapTo[Long]
  }
  def addSaveFile(url:String,description:String,label:Int,secret:Int,userId:Long,insertTime:Long)={



    val mama=List("UTF-8","GBK")

    for (ma<-mama) {
      try{
        val webcontent = Source.fromURL(url,ma).getLines()

        var ii = 0
        println("%%%%%%%%%%%%%% " +ma)
        if (webcontent.nonEmpty) {
          for (line <- webcontent) {
            ii += 1
          }
          println("#############################" + ii)
          if (ii > 20) {
            val flag = 1
           val id= insertSave(url,description,secret,userId,insertTime,webcontent.toString(),flag)
            val pattern = "[!-~]+".r
            id.map{saveId=>
              db.run(uLabel.map(t=>(t.taskId,t.labelNum,t.userId)).returning(
                uLabel.map(_.id))+=(saveId,label,userId)).mapTo[Long]
              val out = new PrintWriter(new File("D://project//WebCollections//public//web" + "//" + saveId  + ".html"), "utf-8")
              val outed = new PrintWriter(new File("D://project//WebCollections//public//source" + "//" + saveId + ".txt"), "utf-8")
              val webcontents = Source.fromURL(url,ma).getLines()
              for (linel <- webcontents) {
                out.println(linel)
                val myline = pattern.replaceAllIn(linel, "").trim
                outed.println(myline.replaceAll("  ", ""))
              }
              out.close()
              outed.close()
            }
          }
        }
      }
      catch{
        case ex:Exception => println(ex)
      }
    }
  }

  def addSave(url:String,description:String,label:Int,secret:Int,userId:Long,insertTime:Long)={
    try{
      Future(addSaveFile(url,description,label,secret,userId,insertTime)).map{r => 1}
    }catch{
      case e:Exception => Future.successful(0)
    }
  }

  def getPersonalSave(userId:Long) = {
    db.run{
      uSave.filter(_.userId===userId).join(uLabel).on(_.id===_.taskId).result}
  }

  def getCommentById(id:Long)={
    db.run{
      uComment.filter(_.saveId===id).result}
  }

  def deletePersonalSave(id:Long,userId:Long)={
    db.run{uSave.filter(s=>(s.id===id)&&(s.userId === userId)).delete}
  }

}
