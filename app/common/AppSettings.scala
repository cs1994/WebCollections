package common

import javax.inject.Inject

import play.api.{Configuration, Environment, Logger}

class AppSettings @Inject()(
                             environment: Environment,
                             configuration: Configuration
                             ) {
  private val log = Logger(this.getClass)
  private[this] val allConfig = configuration

  //mail
  private val mailConf = allConfig.getConfig("mail.conf").get
  val WORLDMALL_EMAIL_ADDRESS = mailConf.getString("WORLDMALL_EMAIL_ADDRESS").getOrElse("156018867@qq.com")
  val WORLDMALL_EMAIL_PASSWORD = mailConf.getString("WORLDMALL_EMAIL_PASSWORD").getOrElse("yanglei1234")
  val SMTP_HOST =  mailConf.getString("SMTP_HOST").getOrElse("smtpcom.263xmail.com")
  val SMTP_PORT =  mailConf.getString("SMTP_PORT").getOrElse("25")
  val POP_SERVER =  mailConf.getString("POP_SERVER").getOrElse("popcom.263xmail.com")
  val POP_PORT =  mailConf.getString("POP_PORT").getOrElse("110")

  //deploy
  private val deployConfig = allConfig.getConfig("deploy.conf").get
//  val deployHost = deployConfig.getString("host").getOrElse("http://localhost:9000/terra/")
  val deployHost = deployConfig.getString("host").getOrElse("localhost:9000/")



}
