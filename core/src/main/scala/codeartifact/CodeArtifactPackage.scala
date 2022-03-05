package codeartifact

import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.Instant

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
    val mvn = if (isScalaProject) {
      sbt.CrossVersion
        .partialVersion(scalaVersion)
        .map {
          case (3, _)     => s"${name}_3"
          case (maj, min) => List(name, "_", maj, ".", min).mkString
        }
        .getOrElse { sys.error("Invalid scalaVersion.") }
    } else {
      name
    }

    sbtBinaryVersion
      .map(mvn + "_" + _)
      .getOrElse(mvn)
  }

  def basePublishPath: String = s"${organization.replace(".", "/")}/$asMaven"
  def versionPublishPath: String = s"$basePublishPath/$version"

  private def currentTimeNumber() =
    DateTimeFormatter
      .ofPattern("yyyyMMddHHmmss")
      .withZone(ZoneId.systemDefault())
      .format(Instant.now())

  def mavenMetadata: String = {
    <metadata modelVersion="1.1.0">
      <groupId>{organization}</groupId>
      <artifactId>{asMaven}</artifactId>
      <versioning>
        <latest>{version}</latest>
        <release>{version}</release>
        <versions>
          <version>{version}</version>
        </versions>
        <lastUpdated>{currentTimeNumber()}</lastUpdated>
      </versioning>
    </metadata>
  }.buildString(stripComments = true)
}
