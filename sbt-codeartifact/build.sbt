enablePlugins(SbtPlugin)

name := "sbt-codeartifact"

// Unfortunately, there's an issue where the Scala compiler is
// erroneously warning on valid SBT syntax.
scalacOptions -= "-Xfatal-warnings"

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.2.8" // set minimum sbt version
  }
}

test := {
  (Test / test).value
  scripted.toTask("").value
}
