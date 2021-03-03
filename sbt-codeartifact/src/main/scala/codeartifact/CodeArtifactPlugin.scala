package codeartifact

import sbt._
import sbt.Keys._

object CodeArtifactPlugin extends AutoPlugin {
  import CodeArtifactKeys._
  import InternalCodeArtifactKeys._

  override def requires = sbt.plugins.JvmPlugin
  override def trigger = allRequirements

  override def buildSettings: Seq[Setting[_]] = buildPublishSettings
  override def projectSettings: Seq[Setting[_]] = codeArtifactSettings

  object autoImport extends CodeArtifactKeys

  def buildPublishSettings: Seq[Setting[_]] = Seq(
    codeArtifactUrl in ThisBuild := ""
  )

  def codeArtifactSettings: Seq[Setting[_]] = Seq(
    codeArtifact := new CodeArtifact(
      codeArtifactRepo.value,
      codeArtifactPackage.value,
      streams.value.log
    ),
    codeArtifactWaitForPackageAvailable := codeArtifact.value.waitForPackageAvailable(),
    codeArtifactUpdateStatus := codeArtifact.value.updatePackageVersionStatus(),
    codeArtifactPublish := {
      // First, do a normal publish.
      publish.value
      // Then, wait for the package to be available on CodeArtifact.
      codeArtifactWaitForPackageAvailable.value
      // Then, manually update the status.
      // See: https://docs.aws.amazon.com/codeartifact/latest/ug/packages-overview.html#package-version-status
      codeArtifactUpdateStatus.value
    },
    codeArtifactRepo := CodeArtifactRepo.fromUrl(codeArtifactUrl.value),
    codeArtifactPackage := CodeArtifactPackage(
      organization = organization.value,
      name = name.value,
      version = version.value,
      scalaVersion = scalaVersion.value
    ),
    publishTo := Some(codeArtifactRepo.value.resolver),
    publishMavenStyle := true,
    credentials += codeArtifact.value.credentials
  ) ++ dependencyOrdering

  def dependencyOrdering: Seq[Setting[_]] = Seq(
    // Although the body of codeArtifactPublish above seems ordered, tasks in the task graph
    // can/will be executed in parallel if they can be. The following few lines define some
    // ordering requirements.
    //
    // We can only wait for the package to be available after it has finished publishing.
    codeArtifactWaitForPackageAvailable := codeArtifactWaitForPackageAvailable
      .dependsOn(publish)
      .value,
    // We can only update the package status after it is made available.
    codeArtifactUpdateStatus := codeArtifactUpdateStatus
      .dependsOn(codeArtifactWaitForPackageAvailable)
      .value
  )

}
