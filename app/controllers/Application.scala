package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import models.dao.{ UserDao}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import util.SecureUtil._
import util.{SecureUtil}
import util.email.EmailUtil
import models.protocols.JsonProtocols

import scala.concurrent.Future

@Singleton
class Application @Inject()( userDao: UserDao,
                             val actionUtils: ActionUtils
                             ) extends Controller with JsonProtocols{

  import actionUtils._
  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  private val log = Logger(this.getClass)
  private val customerAuth = loggingAction


  def login = Action.async {implicit request =>
    Future.successful(Ok(views.html.account.login("用户登录",None)))
  }

}