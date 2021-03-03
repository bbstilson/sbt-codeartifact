# SBT Codeartifact

![Important Badge][badge] [![Maven][maven]][mavenLink]

[badge]: https://img.shields.io/badge/works-on%20my%20machine-success?style=for-the-badge
[maven]: https://img.shields.io/maven-central/v/io.github.bbstilson/sbt-codeartifact_2.12?color=blue&style=for-the-badge
[mavenLink]: https://search.maven.org/artifact/io.github.bbstilson/sbt-codeartifact_2.12

## Install

What you will need:

- AWS CLI v2
- A CodeArtifact repository

Add the following to your sbt `project/plugins.sbt` file:

```scala
addSbtPlugin("io.github.bbstilson" % "sbt-codeartifact" % "0.1.0")
```

## Usage

Note that when specifying `sbt-codeartifact` settings in `project/*.scala` files (as opposed to in the root `build.sbt`), you will need to add the following import:

```scala
import codeartifact.CodeArtifactKeys._
```

## Publishing

```scala
organization := "org.company"
codeartifactUrl := "https://your-domain-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/repo/"
```

### Credentials

