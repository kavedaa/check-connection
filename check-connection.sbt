name := "check-connection"

version := "1.0"

scalaVersion := "3.2.1"

libraryDependencies ++= Seq(
	"org.sphix" %% "sphix" % "1.0-SNAPSHOT",
	"org.shaqal" %% "shaqal-jtds" % "2.0-SNAPSHOT",
	"no.vedaadata" %% "xml-util" % "1.0-SNAPSHOT"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

run / fork := true