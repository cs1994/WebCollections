package controllers.customer


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
import controllers.ActionUtils

@Singleton
class Manage @Inject() (
                         val actionUtils: ActionUtils
                          ) extends Controller with JsonProtocols {

  private [this] val loggingAction = actionUtils.loggingAction

  def setPassword = loggingAction.async { implicit request =>
    Future.successful(Ok(views.html.account.setPassword("设置密码")))
  }

}
