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
  val WORLDMALL_EMAIL_ADDRESS = mailConf.getString("WORLDMALL_EMAIL_ADDRESS").getOrElse("caoshuai@ebupt.com")
  val WORLDMALL_EMAIL_PASSWORD = mailConf.getString("WORLDMALL_EMAIL_PASSWORD").getOrElse("cs1994")
  val SMTP_HOST =  mailConf.getString("SMTP_HOST").getOrElse("smtp.exmail.qq.com")
  val SMTP_PORT =  mailConf.getString("SMTP_PORT").getOrElse("25")
  val POP_SERVER =  mailConf.getString("POP_SERVER").getOrElse("pop.exmail.qq.com")
  val POP_PORT =  mailConf.getString("POP_PORT").getOrElse("110")

  //deploy
  private val deployConfig = allConfig.getConfig("deploy.conf").get
//  val deployHost = deployConfig.getString("host").getOrElse("http://localhost:9000/terra/")
  val deployHost = deployConfig.getString("host").getOrElse("http://localhost:9000/")

  //image
  private val imageConfig = allConfig.getConfig("image").get
  val imageSavePrefix = imageConfig.getString("savePrefix").getOrElse("D:/project/WebCollections/public/headPic")
  val imageAccessPrefix = imageConfig.getString("accessPrefix").getOrElse("/assets/headPic")


}
