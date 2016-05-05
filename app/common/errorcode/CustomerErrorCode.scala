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
  def userNotExist = jsonResult(1006006,"User not exist.")
  def updatePwdFail = jsonResult(1006007,"update password fail.")
  def invalidToken = jsonResult(1006009,"invalid token.")
  def failInsert   = jsonResult(100610,"fail to insert")

  def urlExist = jsonResult(100611,"the url has exists")
  def addSaveFail = jsonResult(100612,"add web url fail")
  def saveListEmpty = jsonResult(100613,"do not have collections")
  def deleteSaveFail = jsonResult(100614,"delete save fail")
  def passwordError = jsonResult(100615,"Password error.")
  def invalidForm = jsonResult(100616,"invalid form.")
  def uploadImageFail = jsonResult(100617,"upload image fail.")
  def addCommentFail = jsonResult(100618,"add comment fail")
  def allreadyExits = jsonResult(100619,"allready exits")
}
