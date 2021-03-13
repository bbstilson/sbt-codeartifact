inThisBuild(
  List(
    organization := "io.github.bbstilson",
    homepage := Some(url("https://github.com/bbstilson/sbt-codeartifact")),
    licenses := Seq("MIT" -> url("https://choosealicense.com/licenses/mit/")),
    developers := List(
      Developer(
        "bbstilson",
        "Brandon Stilson",
        "bbstilson@fastmail.com",
        url("https://github.com/bbstilson")
      )
    )
  )
)

lazy val testSettings: Seq[Setting[_]] = Seq(
  scriptedLaunchOpts := {
    scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
  },
  scriptedBufferLog := false,
  test := {
    (Test / test).value
    scripted.toTask("").value
  }
)

lazy val core = project
  .in(file("core"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-codeartifact-core",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "codeartifact" % "2.16.10"
    )
  )
  .settings(testSettings)

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
  .settings(testSettings)

lazy val root = project
  .in(file("."))
  .aggregate(core, sbtcodeartifact)
  .settings(
    publish / skip := true
  )
