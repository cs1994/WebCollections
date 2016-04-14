package models.dao

import javax.inject.Inject

import com.google.inject.Singleton
import models.tables.SlickTables
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class UserDao @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                         ) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val log = Logger(this.getClass)

  private[this] val uCustomer = SlickTables.tUser

  /**common*/
//  def addUser(email:String,mobile:String,nickName:String,realName:String,headImg:String,state:Int,userType:Int,ip:String,secure:String,bonus:Int,currentTime:Long) = {
//    db.run(uCustomer.map(t=>(t.email,t.mobile,t.nickName,t.realName,t.headImg,t.state,t.userType,t.ip,t.secure,t.bonus,t.insertTime,t.updateTime,t.lastLoginTime)).returning(
//      uCustomer.map(_.id))+=(email,mobile,nickName,realName,headImg,state,userType,ip,secure,bonus,currentTime,currentTime,currentTime)).mapTo[Long].flatMap {uid =>
//      db.run(customerInfo.insertOrUpdate(SlickTables.rCustomerInfo("m",Some(""),uid))).map {id =>
//        if(uid > 0) {
//          uid
//        }else {
//          0L
//        }
//      }
//    }
//  }

  def getUserByEmail(email:String) = {
    log.debug("!!!!")
    db.run(uCustomer.filter(_.email === email).result.headOption)
  }

  def addUser(email:String,headImg:String,sex:Int,phone:String,birthday:String,nickName:String,secure:String,insertTime:Long)={
    db.run(uCustomer.map(t=>(t.email,t.headImg,t.sex,t.phone,t.birthday,t.secure,t.nickName,t.insertTime)).returning(
      uCustomer.map(_.id))+=(email,headImg,sex,phone,birthday,nickName,secure,insertTime)).mapTo[Long]
}
}
