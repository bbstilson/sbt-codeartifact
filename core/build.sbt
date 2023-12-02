enablePlugins(SbtPlugin)

name := "sbt-codeartifact-core"

libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "sso" % "2.21.37",
  "software.amazon.awssdk" % "codeartifact" % "2.21.37",
  "software.amazon.awssdk" % "sts" % "2.21.37",
  "com.lihaoyi" %% "requests" % "0.8.0",
  "com.lihaoyi" %% "os-lib" % "0.9.2",
  "com.lihaoyi" %% "utest" % "0.8.2" % Test
)

testFrameworks += new TestFramework("utest.runner.Framework")
