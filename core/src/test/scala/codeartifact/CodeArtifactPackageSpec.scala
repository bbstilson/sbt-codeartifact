package codeartifact

import utest._

object CodeArtifactPackageSpec extends TestSuite {

  val PackageVersion = "1.2.3"
  val ScalaVersion = "3.2.1"

  val basePackage =
    CodeArtifactPackage("org.example", "name", PackageVersion, ScalaVersion, None, true)

  val tests = Tests {
    test("asMaven") {
      test("scala2") {
        basePackage.copy(scalaVersion = "2.13.5").asMaven ==> "name_2.13"
      }
      test("scala3") {
        basePackage.asMaven ==> "name_3"
      }
      test("java") {
        basePackage.copy(isScalaProject = false).asMaven ==> "name"
      }
      test("sbt") {
        basePackage.copy(sbtBinaryVersion = Some("1.0")).asMaven ==> "name_3_1.0"
      }
    }

    test("versionPublishPath") {
      basePackage.versionPublishPath ==> "org/example/name_3/1.2.3"
    }

    test("mavenMetadata") {
      val xml = scala.xml.XML.loadString(basePackage.mavenMetadata)
      (xml \ "groupId").head.text ==> basePackage.organization
      (xml \ "artifactId").head.text ==> basePackage.asMaven
      (xml \ "versioning" \ "latest").head.text ==> basePackage.version
    }
  }
}
