import javax.inject.{Inject, Singleton}

import org.slf4j.LoggerFactory
import play.api.mvc._

import scala.concurrent.Future

package object controllers {
  import scala.concurrent.ExecutionContext.Implicits.global

  object SessionKey {
    val userId = "uId"
    val email = "email"
    val headImg = "headImg"
    val sex = "sex"
    val phone = "phone"
    val birthday = "birthday"
    val nickName = "nickName"
  }

  @Singleton
  class LoggingAction @Inject()() extends ActionBuilder[Request] {
    val logger = LoggerFactory.getLogger(getClass)
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      logger.info(s"access log: path=${request.path},rawQueryString: ${request.rawQueryString},remoteAddress:${request.remoteAddress}" )

      block(request)
    }
  }

    case class tempUser(
                        id:Long,
                        email:String,
                        nickName:String
                        )

  class CustomerRequest[A](val customer: tempUser, request: Request[A]) extends WrappedRequest[A](request)

  @Singleton
  class CustomerAction @Inject()() extends ActionRefiner[Request,CustomerRequest] {
    val logger = LoggerFactory.getLogger(getClass)
    val SessionTimeOut = 6 * 60 * 60 * 1000 //ms    有效时间6个小时

    protected def authAdmin(request: RequestHeader): Future[Option[tempUser]] = {

      println("****************** request:"+ request.headers+request.session.data)
      val session = request.session
      try {
        val ts = System.currentTimeMillis()
        val id = session.get(SessionKey.userId).get.toLong
        val nickName = session.get(SessionKey.nickName).get.toString
        val email = session.get(SessionKey.email).get.toString

        if (System.currentTimeMillis() - ts > SessionTimeOut) {   // 如果超过两个小时，则自动重新申请一次 TODO
          logger.info("Login failed for timeout")
          Future.successful(None)
        } else{
          Future(Some(tempUser(id,email,nickName)))
        }
      } catch {
        case ex: Throwable =>
          logger.info("Not Login Yet.")
          Future.successful(None)
      }
    }

    protected def onUnauthorized(request: RequestHeader) =
      Results.Redirect("").withNewSession

    override protected def refine[A](request: Request[A]): Future[Either[Result, CustomerRequest[A]]]  = {
      authAdmin(request).map {
        case Some(customer) =>
          Right(new CustomerRequest(customer, request))
        case None =>
          Left(onUnauthorized(request))
      }
    }
  }

  def getCustomerConfMap(request: Request[AnyContent]): Map[String, String] = {
    //    println("........."+ request.cookies)
    lazy val baseInfo = Map(
      "id" -> request.session.get(SessionKey.userId).getOrElse(""),
      "headImg" -> request.session.get(SessionKey.headImg).getOrElse(""),
      "email" -> request.session.get(SessionKey.email).getOrElse(""),
      "nickName" -> request.session.get(SessionKey.nickName).getOrElse(""),
      "sex" -> request.session.get(SessionKey.sex).getOrElse(""),
      "phone" -> request.session.get(SessionKey.phone).getOrElse(""),
      "birthday" -> request.session.get(SessionKey.birthday).getOrElse("")
    )
    baseInfo
  }

//  def getEmptyConfMap = {
  //    Map(
  //      "id" -> "",
  //      "headimg" -> "",
  //      "nickName" ->"",
  //      "utype" ->"",
  //      "logintype" ->""
  //    )
  //  }

  @Singleton
  case class ActionUtils @Inject()(
                                    loggingAction: LoggingAction,
//                                    adminAction: AdminAction,
                                    customerAction: CustomerAction
                                    ) {
  }

}
