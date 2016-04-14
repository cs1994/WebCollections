package common.errorcode

import models.protocols.BaseJsonProtocols
import play.api.libs.json.JsValue

object CustomerErrorCode extends BaseJsonProtocols{
  /**
   * 顾客相关错误码 1006000 --1006999
   */
  def missingParameters = jsonResult(1006000,"missing parameters !")
  def invalidEmailFormat = jsonResult(1006001,"invalid email format.")
  def sendConfirmEmailFail = jsonResult(1006002,"send confirm email fail.")
  def registerFail = jsonResult(1006003,"register customer fail")
  def emailAlreadyExisted = jsonResult(1006004,"email has already existed")
  def invalidEmailToken = jsonResult(10060005,"invalid email token")

}
