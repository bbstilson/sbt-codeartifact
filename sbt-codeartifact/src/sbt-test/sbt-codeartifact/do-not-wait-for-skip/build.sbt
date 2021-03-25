name := "do-not-wait-for-skip-test"
scalaVersion := "2.13.5"

// This is a real url, and it must be, but only I'll be able to
// use it since only I have the aws creds to get an auth token.
val repoUrl = "https://bbstilson-968410040515.d.codeartifact.us-west-2.amazonaws.com/maven/test/"
codeArtifactUrl := repoUrl

// We set this to skip, and so this test is essentially a noop.
// Previous behavior would fail as it would be waiting for a package that did not exist.
// See: https://github.com/bbstilson/sbt-codeartifact/issues/2
publish / skip := true
