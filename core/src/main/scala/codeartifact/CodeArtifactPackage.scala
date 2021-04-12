package codeartifact

final case class CodeArtifactPackage(
  organization: String,
  name: String,
  version: String,
  scalaVersion: String,
  sbtBinaryVersion: Option[String],
  isScalaProject: Boolean
) {

  def asMaven: String = {
    // We only want to append the scala binary version if we are publishing a scala library.
    // This can be deterined by the `crossPaths` sbt flag, for example.
    // See: https://www.scala-sbt.org/1.x/docs/Cross-Build.html#Scala-version+specific+source+directory
    val mvn = if (isScalaProject) {
      sbt.CrossVersion
        .partialVersion(scalaVersion)
        .map { case (maj, min) => List(name, "_", maj, ".", min).mkString }
        .getOrElse { sys.error("Invalid scalaVersion.") }
    } else {
      name
    }

    sbtBinaryVersion
      .map(mvn + "_" + _)
      .getOrElse(mvn)
  }

}
