name := "resolver-test"
scalaVersion := "2.13.5"

// This is a real url, and it must be, but only I'll be able to
// use it since only I have the aws creds to get an auth token.
val repoUrl = "https://bbstilson-968410040515.d.codeartifact.us-west-2.amazonaws.com/maven/test/"

codeArtifactUrl := repoUrl
// Adding the same repo again.
// We should see the same credentials and resolvers twice.
codeArtifactResolvers := List(repoUrl)

lazy val checkResolversAndCredentials = taskKey[Unit](
  "Ensures that resolvers and credentials are added for the" +
    "codeArtifactUrl repo and any repos added via codeArtifactResolvers."
)

checkResolversAndCredentials := {
  val repos = resolvers.value.map(_.name)
  val creds = credentials.value

  assert(repos.size == 2, "CodeArtifact resolvers were not added.")
  assert(creds.size == 2, "CodeArtifact credentials were not added.")
  assert(creds.head.toString() == creds.last.toString(), "Credentials did not match.")
  assert(repos.head == repos.last, "Resolvers did not match.")

  streams.value.log.success("Added credentials and resolvers.")
}
