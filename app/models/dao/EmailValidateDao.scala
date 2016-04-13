package models.dao

import javax.inject.Inject
import models.tables.SlickTables._
import org.slf4j.LoggerFactory
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

class EmailValidateDao @Inject()(
                                  protected val dbConfigProvider: DatabaseConfigProvider
                                  ) extends HasDatabaseConfigProvider[JdbcProfile] {
  private[this] val logger = LoggerFactory.getLogger(this.getClass)


  def add(email:String,token:String,expiredTime:Long,addTime:Long) = {
    db.run{
      tEmail.map{r=>
        (r.email,r.token,r.expiredTime,r.addTime)
      }.returning(tEmail.map(_.id)) +=(email,token,expiredTime,addTime)
    }.mapTo[Long]
  }

  def getByToken(token:String) =  {
    db.run{
      tEmail.filter(_.token===token).sortBy(_.addTime.desc).result.headOption
    }
  }

  def updateStateByToken(token:String,state:Int,curTimestamp:Long) = {
    db.run{
      tEmail.filter(v => v.token===token&&v.expiredTime>curTimestamp).map(_.validate).update(state)
    }
  }
}
