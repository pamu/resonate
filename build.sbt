name := """resonate"""

version := """1.0.0"""

scalaVersion := """2.10.4"""

sbtVersion := """0.13.8"""

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.3-M1",
	"com.typesafe.slick" %% "slick" % "2.1.0",
	"org.webjars" % "bootstrap" % "3.3.4",
	"org.webjars" % "requirejs" % "2.1.11-1",
	"org.webjars" % "jquery" % "1.11.2"
)

//PlayScala
lazy val root = (project in file(".")).enablePlugins(PlayScala)
