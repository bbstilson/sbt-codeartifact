enablePlugins(SbtPlugin)

name := "sbt-codeartifact-core"

libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "codeartifact" % "2.17.48",
  //"software.amazon.awssdk" % "sts" % "2.17.48",
  "com.lihaoyi" %% "requests" % "0.6.9",
  "com.lihaoyi" %% "os-lib" % "0.7.8",
  "com.lihaoyi" %% "utest" % "0.7.10" % Test
)

testFrameworks += new TestFramework("utest.runner.Framework")
