package controllers

import java.io.{PrintWriter, File}

import com.github.nscala_time.time.Imports._
import util.webSave.HttpClientUtil
import org.apache.http.client.protocol.HttpClientContext


object WebGet {
  private final val httpTool = new HttpClientUtil()
  private final val httpContext = HttpClientContext.create()

  def getUrlContent(url:String) = {
    httpTool.getResponseContent(url,httpContext,"UTF-8")
  }

  def getUrlContent2(url:String,charset:String) = {
    httpTool.getResponseContent(url,httpContext,charset)
  }

  def saveToFile(fileName:String,content:String) = {
    val writer = new PrintWriter(new File(fileName),"utf-8")
    writer.write(content)
    writer.close()
  }

  def main(args: Array[String]) {
    val url = "https://www.baidu.com/"
    val content = httpTool.getResponseContent(url,"UTF-8")
    val content2 = getUrlContent(url)

    saveToFile("baidu.html",content2)

    println(content)
  }



}
