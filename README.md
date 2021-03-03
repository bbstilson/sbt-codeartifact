# SBT CodeArtifact

![Important Badge][badge] [![Maven][maven]][mavenLink]

[badge]: https://img.shields.io/badge/works-on%20my%20machine-success?style=for-the-badge
[maven]: https://img.shields.io/maven-central/v/io.github.bbstilson/sbt-codeartifact_2.12?color=blue&style=for-the-badge
[mavenLink]: https://search.maven.org/artifact/io.github.bbstilson/sbt-codeartifact_2.12


An sbt plugin for publishing packages to AWS CodeArtifact. It is currently a work in-progress.

## Install

What you will need:

- A CodeArtifact repository

Add the following to your sbt `project/plugins.sbt` file:

```scala
addSbtPlugin("io.github.bbstilson" % "sbt-codeartifact" % version)
```

## Usage

Note that when specifying `sbt-codeartifact` settings in `project/*.scala` files (as opposed to in the root `build.sbt`), you will need to add the following import:

```scala
import codeartifact.CodeArtifactKeys._
```

## Publishing

Here's an example `build.sbt` file:

```scala
organization := "io.github.bbstilson"

name := "foo"

version := "0.1.0"

scalaVersion := "2.13.4"

codeArtifactUrl := "https://io-github-bbstilson-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/private"
```

Then, to publish, run:

```bash
sbt:root> codeArtifactPublish
```

### Credentials

Credentials are resolved using the [DefaultCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html).
