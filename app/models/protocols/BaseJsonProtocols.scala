package models.protocols

import play.api.libs.json.{JsObject, Json}

trait BaseJsonProtocols {
   def jsonResult(errorCode: Int, errorMsg: String) = {
     Json.obj("errcode" -> errorCode, "errmsg" -> errorMsg)
   }

   def jsonResult(errorCode: Int, errorMsg: String, data: JsObject) = {
     Json.obj("errcode" -> errorCode, "errmsg" -> errorMsg) ++ data
   }

   def successResult(data: JsObject) = success ++ data

   val success = jsonResult(0, "ok")
 }
