package util.email

import javax.mail.{Authenticator, PasswordAuthentication}

/**
 * User: Huangshanqi
 * Date: 2015/2/6
 * Time: 18:42
 */
case class MyAuthenticator(userName:String,password:String) extends Authenticator{

   override def getPasswordAuthentication: PasswordAuthentication = {
     new PasswordAuthentication(userName, password)
  }
}
