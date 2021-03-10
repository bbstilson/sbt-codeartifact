package codeartifact

case class CodeArtifactPackage(
  organization: String,
  name: String,
  version: String,
  scalaVersion: String,
  isSbtPlugin: Boolean,
  sbtBinaryVersion: String
) {

  def asMaven: String = {
    val mvn = sbt.CrossVersion
      .partialVersion(scalaVersion)
      .map { case (maj, min) => List(name, "_", maj, ".", min).mkString }
      .getOrElse { sys.error("Invalid scalaVersion.") }

    if (isSbtPlugin) mvn + "_" + sbtBinaryVersion else mvn
  }

}
