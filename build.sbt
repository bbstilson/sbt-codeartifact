val gitUrl = "https://github.com/bbstilson/sbt-codeartifact"

inThisBuild(
  Seq(
    scalaVersion := "2.12.12",
    organization := "io.github.bbstilson",
    homepage := Some(url(gitUrl)),
    licenses := Seq("MIT" -> url(s"$gitUrl/blob/main/LICENSE")),
    description := "Package publisher for AWS CodeArtifact",
    developers := List(
      Developer("bbstilson", "Brandon Stilson", "@bbstilson", url("https://github.com/bbstilson"))
    ),
    scmInfo := Some(
      ScmInfo(url(gitUrl), s"git@github.com:bbstilson/sbt-codeartifact.git")
    ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) {
        Some("snapshots".at(nexus + "content/repositories/snapshots"))
      } else {
        Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
      }
    }
  )
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
