package codeartifact

import software.amazon.awssdk.services.codeartifact.CodeartifactClient
import software.amazon.awssdk.services.codeartifact.model.GetAuthorizationTokenRequest

import sbt._
import scala.concurrent.duration._

object CodeArtifact {

  def mkCredentials(token: String)(repo: CodeArtifactRepo): Credentials = Credentials(
    userName = "aws",
    realm = repo.realm,
    host = repo.host,
    passwd = token
  )

  private def getAuthorizationTokenRequest(domain: String, owner: String) =
    GetAuthorizationTokenRequest
      .builder()
      .domain(domain)
      .domainOwner(owner)
      .durationSeconds(15.minutes.toSeconds)
      .build()

  private def getAuthTokenFromRequest(req: GetAuthorizationTokenRequest): String =
    CodeartifactClient
      .create()
      .getAuthorizationToken(req)
      .authorizationToken()

  def getAuthToken(repo: CodeArtifactRepo): String = {
    val tok = getAuthorizationTokenRequest(repo.domain, repo.owner)
    println((repo.domain, repo.owner))
    println(tok)
    getAuthTokenFromRequest(tok)
  }

  object Defaults {
    val READ_TIMEOUT: Int = 1.minutes.toMillis.toInt
    val CONNECT_TIMEOUT: Int = 5.seconds.toMillis.toInt
  }
}
