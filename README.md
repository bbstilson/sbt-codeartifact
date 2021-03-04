# SBT CodeArtifact

[![Maven][maven]][mavenLink]

[maven]: https://img.shields.io/maven-central/v/io.github.bbstilson/sbt-codeartifact_2.12_1.0?color=blue&style=for-the-badge
[mavenLink]: https://search.maven.org/artifact/io.github.bbstilson/sbt-codeartifact_2.12_1.0

An sbt plugin for publishing/consuming packages to/from AWS CodeArtifact. It is currently a work in-progress.

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

name := "foo"

version := "0.1.0"

scalaVersion := "2.13.4"

codeArtifactUrl := "https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/private"
```

Then, to publish, run:

```bash
sbt:root> codeArtifactPublish
```

## Consuming

A resolver for your repository is added based on the `codeArtifactUrl` key. You can add more repositories manually like so:

```scala
def mkCodeArtifactRepoUrl(name: String): String =
  s"https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/$name"

val additional = List("foo", "superfoo", "other")

codeArtifactUrl := mkCodeArtifactRepoUrl("private")

resolvers ++= additional
  .map(mkCodeArtifactRepoUrl)
  .map(codeartifact.CodeArtifactRepo.fromUrl)
  .map(_.resolver)
```

In sbt:

```sbt
sbt:root> show resolvers
[info] * com-example/foo: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/foo
[info] * com-example/superfoo: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/superfoo
[info] * com-example/other: https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/other
```

## Credentials

Credentials are resolved using the [DefaultCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html).
