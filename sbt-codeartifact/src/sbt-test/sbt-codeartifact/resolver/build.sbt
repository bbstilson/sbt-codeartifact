name := "resolver-test"
scalaVersion := "2.13.5"
// This is a real url, and it must be, but only I'll be able to
// use it since only I have the aws creds to get an auth token.
val repoUrl = "https://bbstilson-968410040515.d.codeartifact.us-west-2.amazonaws.com/maven/test/"
codeArtifactUrl := repoUrl

lazy val checkResolvers = taskKey[Unit](
  "A custom task that ensures that adding a codeArtifactUrl results in a resolver for that repo being added."
)

checkResolvers := {
  val targetDir = target.value
  val resolversFile = targetDir / "resolvers"

  // Write each resolver to the target directory.
  resolvers.value
    .map(_.name)
    .foreach(IO.write(resolversFile, _))

  val resolversText = scala.io.Source.fromFile(resolversFile).mkString

  assert(
    resolversText.contains(codeartifact.CodeArtifactRepo.fromUrl(repoUrl).realm),
    "CodeArtifact resolver was not added (or something else happened. What am I, a precog?)"
  )

  streams.value.log.success("Adding resolver succeeded.")
}
