# SBT CodeArtifact

[![Maven][maven]][mavenlink]

[maven]: https://maven-badges.herokuapp.com/maven-central/io.github.bbstilson/sbt-codeartifact/badge.svg?kill_cache=1&color=blue&style=for-the-badge
[mavenlink]: https://search.maven.org/search?q=g:io.github.bbstilson%20AND%20a:sbt-codeartifact

An sbt plugin for publishing/consuming packages to/from AWS CodeArtifact.

## Install

First, you will need a CodeArtifact repository. Then, add the following to your sbt `project/plugins.sbt` file:

```scala
addSbtPlugin("io.github.bbstilson" % "sbt-codeartifact" % version)
```

## Usage

Note that when specifying `sbt-codeartifact` settings in `project/*.scala` files (as opposed to in the root `build.sbt`), you will need to add the following import:

```scala
import codeartifact.CodeArtifactKeys._
```

## Publishing

CodeArtifact provides instructions on how to connect to your repository. Click "View Connection Instructions", then choose "gradle", then copy the `url`.

Here's an example `build.sbt` file that assumes a CodeArtifact repository named "private" in the "com.example" domain has been created:

```scala
organization := "com.example"

name := "library"

version := "0.1.0"

scalaVersion := "2.13.4"

codeArtifactUrl := "https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/private"
```

Then, to publish, run:

```bash
sbt:library> codeArtifactPublish
```

## Consuming

A resolver for your repository is added based on the `codeArtifactUrl` key. You can add more repositories manually like so:

```scala
val codeArtifactUrlBase = "https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/"

codeArtifactUrl := codeArtifactUrlBase + "release"
codeArtifactResolvers := List("foo", "bar", "baz").map(repo => codeArtifactUrlBase + repo)
```

In sbt:

```plaintext
sbt:library> show resolvers
[info] * com-example/release: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/release
[info] * com-example/foo: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/foo
[info] * com-example/bar: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/superfoo
[info] * com-example/baz: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/other
```

## Credentials

Your CodeArtifact Authentication Token is fetched dynamically using the AWS Java SDK. Credentials are resolved using the [DefaultCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html).

If you would like to provide the token statically (for example, if your AWS creds are unavailable), then you can provide the token as an environment variable:

```bash
export CODEARTIFACT_AUTH_TOKEN=`aws codeartifact get-authorization-token --domain domain-name --domain-owner domain-owner-id --query authorizationToken --output text --profile profile-name`
```

## SBT Release

If you would like to use this in conjunction with [`sbt-release`](https://github.com/sbt/sbt-release), you will need to override the [default release process](https://github.com/sbt/sbt-release#can-we-finally-customize-that-release-process-please); specifically, the publish step:

```scala
import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  // This is the only step that varies.
  releaseStepCommandAndRemaining("codeArtifactPublish"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
```
