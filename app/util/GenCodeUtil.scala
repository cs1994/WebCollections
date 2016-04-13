package util

import scala.util.Random

/**
 * User: Huangshanqi
 * Date: 2015/12/18
 * Time: 11:10
 */
object GenCodeUtil {

  private val random = new Random(System.nanoTime())
  private val smsChars = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
  )

  private val tokenChars = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
  )

  private final val SmsCodeLength = 6
  private final val TokenLength = 64

  def get6SmsCode() = {
    val range = smsChars.length
    (0 until SmsCodeLength).map { _ =>
      smsChars(random.nextInt(range))
    }.mkString("")
  }

  def get64Token() = {
    val range = tokenChars.length
    (0 until TokenLength).map { _ =>
      tokenChars(random.nextInt(range))
    }.mkString("")
  }
}
