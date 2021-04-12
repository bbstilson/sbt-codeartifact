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
    scriptedLaunchOpts.value ++ Seq(
      "-Xmx1024M",
      "-XX:MaxPermSize=256M",
      "-Dplugin.version=" + version.value
    )
  },
  scriptedBufferLog := false
)

lazy val core = project
  .in(file("core"))
  .settings(testSettings)

lazy val `sbt-codeartifact` = project
  .in(file("sbt-codeartifact"))
  .dependsOn(core)
  .settings(testSettings)

lazy val root = project
  .in(file("."))
  .aggregate(core, `sbt-codeartifact`)
  .settings(
    publish / skip := true
  )
