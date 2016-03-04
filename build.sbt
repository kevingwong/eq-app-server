name := """eq-app-server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

pipelineStages := Seq(uglify, digest, gzip)

pipelineStages in Assets := Seq()

pipelineStages := Seq(uglify, digest, gzip)

DigestKeys.algorithms += "sha1"

UglifyKeys.uglifyOps := { js =>
  Seq((js.sortBy(_._2), "concat.min.js"))
}

// excludeFilter in gzip := "*.js"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  evolutions,
  "com.typesafe.play" % "play-java-jdbc_2.11" % "2.4.6",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "org.avaje.ebeanorm" % "avaje-ebeanorm" % "6.14.1",
  "com.google.inject" % "guice" % "4.0",
  "javax.inject" % "javax.inject" % "1",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.webjars" % "angularjs" % "1.3.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",
  "org.webjars" % "jquery" % "2.2.1",
  "org.mockito" % "mockito-core" % "1.10.19" % "test"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
