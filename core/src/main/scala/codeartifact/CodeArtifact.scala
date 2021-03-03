package codeartifact

import sbt._

import software.amazon.awssdk.services.codeartifact.CodeartifactClient
import software.amazon.awssdk.services.codeartifact.model.{
  GetAuthorizationTokenRequest,
  ListPackageVersionsRequest,
  PackageFormat,
  PackageVersionStatus,
  UpdatePackageVersionsStatusRequest
}

import collection.JavaConverters._

class CodeArtifact(
  repo: CodeArtifactRepo,
  pkg: CodeArtifactPackage,
  logger: Logger
) {
  private val client = CodeartifactClient.create()

  private val getAuthorizationTokenRequest = GetAuthorizationTokenRequest
    .builder()
    .domain(repo.domain)
    .domainOwner(repo.owner)
    .durationSeconds(900L) // 15 minutes
    .build()

  private val listPackageVersionsRequest = ListPackageVersionsRequest
    .builder()
    .domain(repo.domain)
    .domainOwner(repo.owner)
    .format(PackageFormat.MAVEN)
    .namespace(pkg.organization)
    .repository(repo.name)
    .packageValue(pkg.asMaven)
    .build()

  private val updatePackageVersionStatusRequest = UpdatePackageVersionsStatusRequest
    .builder()
    .domain(repo.domain)
    .domainOwner(repo.owner)
    .format(PackageFormat.MAVEN)
    .repository(repo.name)
    .namespace(pkg.organization)
    .packageValue(pkg.asMaven)
    .versions(pkg.version)
    .targetStatus(PackageVersionStatus.PUBLISHED)
    .build()

  val credentials: Credentials = Credentials(
    realm = repo.realm,
    host = repo.host,
    userName = "aws",
    passwd = getAuthToken
  )

  private def getAuthToken: String = client
    .getAuthorizationToken(getAuthorizationTokenRequest)
    .authorizationToken()

  def getPackageVersions: List[String] =
    client
      .listPackageVersions(listPackageVersionsRequest)
      .versions()
      .asScala
      .toList
      .map(_.version())

  def updatePackageVersionStatus(): Unit = {
    val resp = client
      .updatePackageVersionsStatus(updatePackageVersionStatusRequest)

    val failedVersions = resp.failedVersions().asScala.toMap
    if (failedVersions.nonEmpty) {
      failedVersions.foreach { case (k, err) =>
        logger.error(s"$k - ${err.errorCodeAsString()}:  ${err.errorMessage()}")
      }
      sys.error("Did not successfully update packages.")
    }

    resp.successfulVersions().asScala.foreach { case (k, success) =>
      logger.info(s"$k - ${success.statusAsString()}")
    }
  }

  def waitForPackageAvailable(
    attempts: Int = 0
  ): Unit = {
    if (!getPackageVersions.contains(pkg.version)) {
      if (attempts > CodeArtifact.MAX_RETRY) {
        sys.error(
          "Unable to resolve package in CodeArtifacts. Please check for errors and try again."
        )
      } else {

        println(
          s"Package not available yet... Will check again in ${CodeArtifact.REQUEST_DELAY}ms."
        )
        Thread.sleep(CodeArtifact.REQUEST_DELAY)
        waitForPackageAvailable(attempts + 1)
      }
    }
  }

}

object CodeArtifact {
  private[codeartifact] val REQUEST_DELAY = 2000L // 2 seconds
  // Will wait up to 30 seconds before giving up.
  private[codeartifact] val MAX_RETRY = 15
}
