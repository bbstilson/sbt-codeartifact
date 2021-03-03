val gitUrl = "https://github.com/bbstilson/sbt-codeartifact"

ThisBuild / version := "0.0.4"
ThisBuild / scalaVersion := "2.12.12"
ThisBuild / organization := "io.github.bbstilson"
ThisBuild / homepage := Some(url(gitUrl))
ThisBuild / licenses := Seq("MIT" -> url(s"$gitUrl/blob/main/LICENSE"))
ThisBuild / description := "Package publisher for AWS CodeArtifact"
ThisBuild / developers := List(
  Developer("bbstilson", "Brandon Stilson", "@bbstilson", url("https://github.com/bbstilson"))
)
ThisBuild / scmInfo := Some(
  ScmInfo(url(gitUrl), s"git@github.com:bbstilson/sbt-codeartifact.git")
)

lazy val core = project
  .in(file("core"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-codeartifact-core",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "codeartifact" % "2.16.10"
    ),
    scalacOptions -= "-Xfatal-warnings"
  )

lazy val sbtcodeartifact = project
  .in(file("sbt-codeartifact"))
  .enablePlugins(SbtPlugin)
  .dependsOn(core)
  .settings(
    name := "sbt-codeartifact",
    scalacOptions -= "-Xfatal-warnings",
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.2.8" // set minimum sbt version
      }
    }
  )

lazy val root = project
  .in(file("."))
  .aggregate(core, sbtcodeartifact)
  .settings(
    publish / skip := true
  )
