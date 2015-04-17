name := """resonate"""

version := """1.0.0"""

sbtVersion := """0.13.7"""

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.3-M1",
	"com.typesafe.slick" %% "slick" % "2.1.0",
	"org.webjars" % "bootstrap" % "3.3.4",
	"org.webjars" % "jquery" % "2.1.3",
	"org.webjars" % "requirejs" % "2.1.11-1"
)

//PlayScala
lazy val root = (project in file(".")).enablePlugins(PlayScala)
