import sbt._
import Keys._

object UWTimeDemo extends Build {

  lazy val dependencies = Seq(
    "edu.uw.cs.lil" % "uwtime-standalone" % "1.0.1"
  )

  val projectName = "uw-time-demo"

  lazy val projectSettings = Seq(
    name := projectName,
    scalaVersion := "2.11.8",
    libraryDependencies ++= dependencies,
    version := "1.0",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases")
    )

  )

  lazy val root =
    Project(projectName, file("."))
      .configs( IntegrationTest )
      .settings( Defaults.itSettings : _* )
      .settings( projectSettings )
}