enablePlugins(SbtPlugin)

name := "sbt-codeartifact-core"

libraryDependencies ++= Seq(
  "software.amazon.awssdk" % "codeartifact" % "2.16.10",
  "com.lihaoyi" %% "utest" % "0.7.7" % Test
)

testFrameworks += new TestFramework("utest.runner.Framework")
