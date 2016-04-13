package util

import java.util.regex.Pattern

object ValidateUtil {

  private final val mobileRegex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
  private final val mobilePattern = Pattern.compile(mobileRegex)

  private final val emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
  private final val emailPattern = Pattern.compile(emailRegex)


  def isMobileNumber(mobile:String) = {
    mobilePattern.matcher(mobile).matches()
  }

  def isEmail(email:String) = {
    emailPattern.matcher(email).matches()
  }



}
