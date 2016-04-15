package models.dao

import javax.inject.Inject

import com.google.inject.Singleton
import models.tables.SlickTables
import models.tables.SlickTables._
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import util.SecureUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserDao @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                         ) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val log = Logger(this.getClass)

  private[this] val uCustomer = SlickTables.tUser

  /**common*/


  def getUserByEmail(email:String) = {
    db.run(uCustomer.filter(_.email === email).result.headOption)
  }

  def addUser(email:String,headImg:String,sex:Int,phone:String,birthday:String,nickName:String,secure:String,insertTime:Long,ip:String)={
    db.run(uCustomer.map(t=>(t.email,t.headImg,t.sex,t.phone,t.birthday,t.secure,t.nickName,t.insertTime,t.ip)).returning(
      uCustomer.map(_.id))+=(email,headImg,sex,phone,birthday,nickName,secure,insertTime,ip)).mapTo[Long]
}

  def updatePwdByEmail(user:rUser,pwd:String) = {
    val secure = SecureUtil.getSecurePassword(pwd, user.ip, user.insertTime)
    db.run{
      uCustomer.filter(_.email===user.email).map(_.secure).update(secure)
    }.mapTo[Int]
  }
}
