package codeartifact

case class CodeArtifactPackage(
  organization: String,
  name: String,
  version: String,
  scalaVersion: String
) {

  def asMaven: String = sbt.CrossVersion
    .partialVersion(scalaVersion)
    .map { case (maj, min) => s"${name}_${maj}.${min}" }
    .getOrElse { sys.error("Invalid scalaVersion.") }
}
