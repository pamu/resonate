name := """resonate"""

version := """1.0.0"""

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.3-M1",
	"com.typesafe.slick" %% "slick" % "3.0.0",
	"com.zaxxer" % "HikariCP" % "2.3.8",
	"org.webjars" % "bootstrap" % "2.3.1",
	"org.webjars" % "jquery" % "1.9.0",
	"org.webjars" % "requirejs" % "2.1.11-1",
	"joda-time" % "joda-time" % "2.7",
	"org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
	"org.apache.commons" % "commons-email" % "1.3.3",
	"mysql" % "mysql-connector-java" % "5.1.35"
)

//PlayScala
lazy val root = (project in file(".")).enablePlugins(PlayScala)
