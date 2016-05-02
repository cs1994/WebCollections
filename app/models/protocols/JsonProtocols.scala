package models.protocols

import models.tables.SlickTables._
import play.api.libs.json.{JsValue, Json, Writes}

trait JsonProtocols extends BaseJsonProtocols{
 implicit val rUserWriter: Writes[rUser] = new Writes[rUser] {
  override def writes(obj: rUser): JsValue = {
   Json.obj (
    "id" -> obj.id,
    "email" -> obj.email,
    "headImg" -> obj.headImg,
    "sex" -> obj.sex,
    "phone" -> obj.phone,
    "birthday" -> obj.birthday,
    "nickName" -> obj.nickName,
    "insertTime" -> obj.insertTime,
    "commentNum" -> obj.commentNum
   )
  }
 }
 }

