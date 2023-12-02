package codeartifact

import sbt._

case class CodeArtifactRepo(
  name: String,
  domain: String,
  host: String,
  owner: String,
  url: String
) {
  def realm: String = s"$domain/$name"
  def resolver: MavenRepository = realm.at(url)
}

object CodeArtifactRepo {

  def fromUrl(url: String): CodeArtifactRepo = {
    if (url.isEmpty()) {
      sys.error("""codeArtifactUrl not defined. assign this with codeArtifactUrl := "your-url"""")
    }

    // Url looks like:
    // https://<domain>-<owner>.d.codeartifact.us-west-2.amazonaws.com/maven/<repository>
    val juri = new java.net.URI(url)

    // Split on slashes, and get the last element: <repository>.
    val name = juri.getPath().split('/').last

    // Split on dots. Take the head, which is the <domain>-<owner> section.
    // Split on dashes.
    val host = juri.getHost()
    val parts = host.split('.').head.split('-')
    // Last element is <owner>.
    val owner = parts.last
    // The rest are the <domain>. Merge them together again.
    val domain = parts.init.mkString("-")

    CodeArtifactRepo(
      name = name,
      domain = domain,
      host = host,
      owner = owner,
      url = url
    )
  }
}
