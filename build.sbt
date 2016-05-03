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
  val javaMailVersion = "1.5.3"
  val scalaXmlV = "1.0.4"
  val playSlickV = "1.1.1"
  val slickV = "3.1.0"
  val httpclientVersion = "4.3.5"
  val httpcoreVersion = "4.3.2"

  Seq(
    "com.typesafe.play" %% "play-slick" % playSlickV,
    "org.scala-lang.modules" % "scala-xml_2.11" % scalaXmlV,
    "com.typesafe.slick" %% "slick" % slickV withSources(),
    "com.typesafe.slick" %% "slick-codegen" % slickV,
    "com.github.nscala-time" %% "nscala-time" % nscalaTimeV,
    "commons-codec" % "commons-codec" % codecV,
    "mysql" % "mysql-connector-java" % mysqlConnectorV,
    "com.sun.mail" % "javax.mail" % javaMailVersion withSources(),
    "org.apache.httpcomponents" % "httpclient" % httpclientVersion withSources(),
    "org.apache.httpcomponents" % "httpcore" % httpcoreVersion withSources(),
    "org.apache.httpcomponents" % "httpmime" % httpclientVersion withSources(),
    "org.apache.commons" % "commons-collections4" % "4.0"
  )
}


// frontend
libraryDependencies ++= Seq(
  "org.webjars" % "webjars-play_2.11" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "react" % "0.13.3",
  "org.webjars.bower" % "react-router" % "0.13.3",
  "org.webjars.bower" % "reflux" % "0.2.11",
  "org.webjars" % "toastr" % "2.1.0",
  "org.webjars" % "font-awesome" % "4.4.0",
  "org.webjars.bower" % "smalot-bootstrap-datetimepicker" % "2.3.1",
  "org.webjars" % "momentjs" % "2.10.6",
  "org.webjars.bower" % "bootstrap-daterangepicker" % "2.0.11"

)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "sonatype-forge" at "https://repository.sonatype.org/content/groups/forge/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
