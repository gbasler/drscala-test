import sbt._
import Keys._

object build extends Build {
  lazy val root = Project(
    id = "root",
    base = file("."),
    aggregate = Seq(main),
    settings = sharedSettings
  )

  lazy val sharedSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.3"
  )

  def GitHubApi = "org.kohsuke" % "github-api" % "1.44"

  def Config = "com.typesafe" %  "config" % "1.0.2"

  def DrScala = "com.github.aloiscochard" %% "drscala" % "0.1.0-SNAPSHOT"

  // Scalac command line options to install our compiler plugin.
  lazy val usePluginSettings = Seq(
    autoCompilerPlugins := true,
    addCompilerPlugin(DrScala),
   scalacOptions in Compile ++= {
      Seq(
        "debug",
        "warn"
      ).map("-P:drscala:" + _)
    },
    libraryDependencies ++= Seq(GitHubApi % Configurations.ScalaTool, Config)
    //    javaOptions in Compile := Seq("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005")
  )

  // A regular module with the application code.
  lazy val main = Project(
    id = "main",
    base = file("main"),
    settings = sharedSettings ++ usePluginSettings ++ Seq(
      publishArtifact in Compile := false
    )
  )
}
