package common.email

import scala.concurrent.ExecutionContext.Implicits.global
import java.util.{Properties, Date}
import javax.mail.Message.RecipientType
import javax.mail.{Session, Transport}
import javax.mail.internet.{MimeBodyPart, MimeMultipart, InternetAddress, MimeMessage}
import com.google.inject.{Inject, Singleton}
import common.AppSettings
import util.email.{EmailUtil, MyAuthenticator}

import scala.concurrent.Future
import scala.util.{Failure, Success}


@Singleton
class Email @Inject()(
                            appSettings: AppSettings
                            ){
  var sendSuccess = false
  var sendConfirm = false

  def getProperties = {
    val p = new Properties
    p.put("mail.smtp.host", appSettings.SMTP_HOST)
    p.put("mail.smtp.port", appSettings.SMTP_PORT)
    p.put("mail.transport.protocol", "smtp")
    p.put("mail.smtp.auth", "true")
    p
  }
  val props = getProperties
  def getEbuptSession = {
    Session.getInstance(props,new MyAuthenticator(appSettings.WORLDMALL_EMAIL_ADDRESS, appSettings.WORLDMALL_EMAIL_PASSWORD))
  }
  def sendRegisterEmail(token:String,email:String) = {
    val confirmUrl = s"${appSettings.deployHost}mail/validate_register?token=$token"

    val session = getEbuptSession

    val FROM = appSettings.WORLDMALL_EMAIL_ADDRESS

    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(FROM))


    message.setRecipient(RecipientType.TO,new InternetAddress(email))
    message.setSubject("欢迎加入")
    message.setSentDate(new Date)
    val mainPart = new MimeMultipart
    val html = new MimeBodyPart
    html.setContent(EmailUtil.getRegisterEamilHtml(appSettings.deployHost,confirmUrl,email), "text/html; charset=utf-8")
    mainPart.addBodyPart(html)
    message.setContent(mainPart)
    Transport.send(message)
  }
  def SendRegisterEmailTask(token:String,email:String)={
    Future{
      sendRegisterEmail(token,email)
    }.onComplete{
      case Success(bool) => {
        sendSuccess = true
      }
      case Failure(e) =>{
        sendSuccess =  false
      }
    }
  }
  def sendConfirmEmail(token:String,email:String) = {

    val confirmUrl = s"${appSettings.deployHost}mail/validate_confirm?token=$token"

    val session = getEbuptSession

    val FROM = appSettings.WORLDMALL_EMAIL_ADDRESS

    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(FROM))

    message.setRecipient(RecipientType.TO,new InternetAddress(email))
    message.setSubject("重置密码信息")
    message.setSentDate(new Date)
    val mainPart = new MimeMultipart
    val html = new MimeBodyPart
    html.setContent(EmailUtil.getConfirmEmailHtml(appSettings.deployHost,confirmUrl,email), "text/html; charset=utf-8")
    mainPart.addBodyPart(html)
    message.setContent(mainPart)
    Transport.send(message)
  }
  def SendConfirmEmailTask(token:String,email:String)={
    Future{
      sendConfirmEmail(token,email)
    }.onComplete{
      case Success(bool) => {
        sendConfirm = true
      }
      case Failure(e) =>{
        sendConfirm =  false
      }
    }
  }

}
