name := """resonate"""

version := """1.0.0"""

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.3-M1",
	"com.typesafe.slick" %% "slick" % "2.1.0",
	"org.webjars" % "bootstrap" % "2.3.1",
	"org.webjars" % "jquery" % "1.9.0",
	"org.webjars" % "requirejs" % "2.1.11-1",
	"joda-time" % "joda-time" % "2.7",
	"org.postgresql" % "postgresql" % "9.4-1200-jdbc41"
)

//PlayScala
lazy val root = (project in file(".")).enablePlugins(PlayScala)
