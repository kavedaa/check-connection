name := "check-connection"

version := "1.0"

scalaVersion := "3.2.1"

resolvers += "My Maven Repo Resolver" at "https://mymavenrepo.com/repo/pINely5F8nmLUayJnPul/"

libraryDependencies ++= Seq(
	"org.sphix" %% "sphix" % "1.20.13.6",
	"org.shaqal" %% "shaqal-jtds" % "2.0-SNAPSHOT",
	"no.vedaadata" %% "xml-util" % "1.0-SNAPSHOT"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

run / fork := true

assemblyMergeStrategy := {
  case PathList(ps @ _*) if ps.last endsWith ".json" => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last endsWith "resourcebundles" => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last endsWith "module-info.class" => MergeStrategy.discard
  case x => assemblyMergeStrategy.value(x)
}

assembly / target := file("assembly")