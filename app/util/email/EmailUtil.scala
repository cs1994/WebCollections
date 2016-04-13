package util.email

import java.util.{Date, Properties}
import javax.mail.Message.RecipientType
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}
import javax.mail.{Session, Transport}

import com.github.nscala_time.time.Imports._

object EmailUtil {
  val props = getProperties

  def sendResetPasswordEmail(password:String,email:String) = {


    val session = getEbuptSession

    val FROM = "cwtc@tnetstar.com"

    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(FROM))

    message.setRecipient(RecipientType.TO,new InternetAddress(email))
    message.setSubject("国贸社区重置密码信息")
    message.setSentDate(new Date)
    val mainPart = new MimeMultipart
    val html = new MimeBodyPart
    html.setContent(getConfirmEmailHtml("cwtc@tnetstar.com",password,email), "text/html; charset=utf-8")
    mainPart.addBodyPart(html)
    message.setContent(mainPart)
    Transport.send(message)
  }

  def getEbuptSession = {
    Session.getInstance(props,new MyAuthenticator("cwtc@tnetstar.com", "tnet1234567"))
  }

  def getProperties = {
    val p = new Properties
    p.put("mail.smtp.host", "smtpcom.263xmail.com")
    p.put("mail.smtp.port", "25")
    p.put("mail.transport.protocol", "smtp")
    p.put("mail.smtp.auth", "true")
    p
  }

  def sendPasswordEmail(deployHost:String,password:String,email:String):String={

    val sb: StringBuilder = new StringBuilder


    sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>")

    sb.append("""<table width="100%" bgcolor="#f4f9fd" cellpadding="0" cellspacing="10"><tbody>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><b><font size="4" color="#555555" face="Arial, Helvetica, sans-serif">你好， <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); z-index: 1; position: static;" t="7" onclick="return false;"  isout="1">${email}</span></font></b><br><font size="3" color="#555555" face="Arial, Helvetica, sans-serif">欢迎来到国贸社区，您的商户登录密码是：</font></td></tr>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><font>${password}</font><br></td></tr>""")
    sb.append(s"""<tr>	<td height="40" valign="top">	<font size="3" color="#555555" face="Arial, Helvetica, sans-serif">祝使用愉快！<br>国贸 <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); position: relative;" >${DateTime.now.toString("yyyyMMdd HH:mm:ss")}<br>	</font></td></tr>""")
    sb.append(s"""<tr><td height="80" valign="top"><font size="2" color="#909090" face="Arial, Helvetica, sans-serif">如果你没有注册过国贸社区，请忽略此邮件。<br>		这是国贸社区的帐号服务邮件，请不要回复。<br>如需了解国贸社区或找回密码遇到问题，请访问我们的帮助中心 (连接至<a href="$deployHost" target="_blank"><font size="2" color="#339adf" face="Arial, Helvetica, sans-serif">$deployHost</font></a>)</font>	</td></tr>""")
    sb.append("""</tbody></table>""")

    sb.append("</body></html>")
    sb.toString()
  }

  def getConfirmEmailHtml(deployHost:String,confirmUrl:String,email:String):String={

    val sb: StringBuilder = new StringBuilder


    sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>")

    sb.append("""<table width="100%" bgcolor="#f4f9fd" cellpadding="0" cellspacing="10"><tbody>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><b><font size="4" color="#555555" face="Arial, Helvetica, sans-serif">你好， <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); z-index: 1; position: static;" t="7" onclick="return false;"  isout="1">${email}</span></font></b><br><font size="3" color="#555555" face="Arial, Helvetica, sans-serif">请点击下面的链接重置密码：</font></td></tr>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><a href="$confirmUrl" target="_blank"><font size="3" color="#339adf" face="Arial, Helvetica, sans-serif"></font>$confirmUrl</a><font></font><br><font size="3" color="#909090" face="Arial, Helvetica, sans-serif">(此链接1天内有效，超时需要重新获取邮件)</font></td></tr>""")
    sb.append(s"""<tr>	<td height="40" valign="top">	<font size="3" color="#555555" face="Arial, Helvetica, sans-serif">祝使用愉快！<br>国贸 <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); position: relative;" >${DateTime.now.toString("yyyyMMdd HH:mm:ss")}<br>	</font></td></tr>""")
    sb.append(s"""<tr><td height="80" valign="top"><font size="2" color="#909090" face="Arial, Helvetica, sans-serif">如果你没有注册过国贸社区，请忽略此邮件。<br>		这是国贸社区的帐号服务邮件，请不要回复。<br>如需了解国贸社区或找回密码遇到问题，请访问我们的帮助中心 (连接至<a href="$deployHost" target="_blank"><font size="2" color="#339adf" face="Arial, Helvetica, sans-serif">$deployHost</font></a>)</font>	</td></tr>""")
    sb.append("""</tbody></table>""")

    sb.append("</body></html>")
    sb.toString()
  }

  def getRegisterEamilHtml(deployHost:String,confirmUrl:String,email:String) = {
    val sb: StringBuilder = new StringBuilder


    sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>")

    sb.append("""<table width="100%" bgcolor="#f4f9fd" cellpadding="0" cellspacing="10"><tbody>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><b><font size="4" color="#555555" face="Arial, Helvetica, sans-serif">你好， <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); z-index: 1; position: static;" t="7" onclick="return false;"  isout="1">${email}</span></font></b><br><font size="3" color="#555555" face="Arial, Helvetica, sans-serif">请点击下面的链接激活注册邮箱：</font></td></tr>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><a href="$confirmUrl" target="_blank"><font size="3" color="#339adf" face="Arial, Helvetica, sans-serif"></font>$confirmUrl</a><font></font><br><font size="3" color="#909090" face="Arial, Helvetica, sans-serif">(此链接1天内有效，超时需要重新获取邮件)</font></td></tr>""")
    sb.append(s"""<tr>	<td height="40" valign="top">	<font size="3" color="#555555" face="Arial, Helvetica, sans-serif">祝使用愉快！<br>国贸 <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); position: relative;" >${DateTime.now.toString("yyyyMMdd HH:mm:ss")}<br>	</font></td></tr>""")
    sb.append(s"""<tr><td height="80" valign="top"><font size="2" color="#909090" face="Arial, Helvetica, sans-serif">如果你没有注册过国贸社区，请忽略此邮件。<br>		这是国贸社区的帐号服务邮件，请不要回复。<br>如需了解国贸社区或找回密码遇到问题，请访问我们的帮助中心 (连接至<a href="$deployHost" target="_blank"><font size="2" color="#339adf" face="Arial, Helvetica, sans-serif">$deployHost</font></a>)</font>	</td></tr>""")
    sb.append("""</tbody></table>""")

    sb.append("</body></html>")
    sb.toString()
  }

  def getBindEamilHtml(deployHost:String,confirmUrl:String,email:String) = {
    val sb: StringBuilder = new StringBuilder


    sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>")

    sb.append("""<table width="100%" bgcolor="#f4f9fd" cellpadding="0" cellspacing="10"><tbody>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><b><font size="4" color="#555555" face="Arial, Helvetica, sans-serif">你好， <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); z-index: 1; position: static;" t="7" onclick="return false;"  isout="1">${email}</span></font></b><br><font size="3" color="#555555" face="Arial, Helvetica, sans-serif">请点击下面的链接激活绑定邮箱：</font></td></tr>""")
    sb.append(s"""<tr>	<td height="50" valign="top"><a href="$confirmUrl" target="_blank"><font size="3" color="#339adf" face="Arial, Helvetica, sans-serif"></font>$confirmUrl</a><font></font><br><font size="3" color="#909090" face="Arial, Helvetica, sans-serif">(此链接1天内有效，超时需要重新获取邮件)</font></td></tr>""")
    sb.append(s"""<tr>	<td height="40" valign="top">	<font size="3" color="#555555" face="Arial, Helvetica, sans-serif">祝使用愉快！<br>国贸 <span style="border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 204, 204); position: relative;" >${DateTime.now.toString("yyyyMMdd HH:mm:ss")}<br>	</font></td></tr>""")
    sb.append(s"""<tr><td height="80" valign="top"><font size="2" color="#909090" face="Arial, Helvetica, sans-serif">如果你没有注册过国贸社区，请忽略此邮件。<br>		这是国贸社区的帐号服务邮件，请不要回复。<br>如需了解国贸社区或找回密码遇到问题，请访问我们的帮助中心 (连接至<a href="$deployHost" target="_blank"><font size="2" color="#339adf" face="Arial, Helvetica, sans-serif">$deployHost</font></a>)</font>	</td></tr>""")
    sb.append("""</tbody></table>""")

    sb.append("</body></html>")
    sb.toString()
  }
}
