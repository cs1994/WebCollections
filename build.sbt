name := """WebColletions"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)



libraryDependencies ++= {

  val nscalaTimeV = "2.6.0"
  val mysqlConnectorV = "5.1.31"
  val codecV = "1.9"


  Seq(
    "mysql" % "mysql-connector-java" % mysqlConnectorV,
    "com.typesafe.play" %% "anorm" % "2.4.0",
    "com.github.nscala-time" %% "nscala-time" % nscalaTimeV,
    "commons-codec" % "commons-codec" % codecV
  )

}


// frontend
libraryDependencies ++= Seq(
  "org.webjars" % "webjars-play_2.11" % "2.4.0-1",
  //  "org.webjars" % "bootstrap" % "3.3.5",
    "org.webjars" % "react" % "0.13.3",
    "org.webjars.bower" % "react-router" % "0.13.3",
    "org.webjars.bower" % "reflux" % "0.2.11",
  "org.webjars" % "toastr" % "2.1.0",
    "org.webjars" % "font-awesome" % "4.4.0"
  //  "org.webjars.bower" % "smalot-bootstrap-datetimepicker" % "2.3.1",//时间控件
  //  "org.webjars" % "momentjs" % "2.10.6",//时间转换
  //  "org.webjars.bower" % "bootstrap-daterangepicker" % "2.0.11",//时间区间控件
  //  "org.webjars" % "lodash" % "3.10.1",//js扩展工具
  //  "org.webjars" % "less" % "2.5.3",//.less文件预编译为css
  //  "org.webjars" % "hammerjs" % "2.0.4",//绘图
  //  "org.webjars.bower" % "raphael" % "2.1.4"//绘图
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "sonatype-forge" at "https://repository.sonatype.org/content/groups/forge/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
