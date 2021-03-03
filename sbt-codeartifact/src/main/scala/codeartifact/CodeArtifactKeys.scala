package codeartifact

import sbt._

trait CodeArtifactKeys {

  ///////////
  // Tasks //
  //////////.

  val codeArtifactPublish: TaskKey[Unit] =
    taskKey[Unit]("Publish to AWS CodeArtifact.")

  //////////////
  // Settings //
  //////////////

  val codeArtifactUrl: SettingKey[String] =
    settingKey[String]("CodeArtifact connection url.")
}

trait InternalCodeArtifactKeys {
  val codeArtifact = taskKey[CodeArtifact]("Covenience wrapper around a repo and package.")
  val codeArtifactRepo = taskKey[CodeArtifactRepo]("CodeArtifact repository.")
  val codeArtifactPackage = taskKey[CodeArtifactPackage]("CodeArtifact package.")

  val codeArtifactWaitForPackageAvailable =
    taskKey[Unit]("Waits for the package to be availble so that the status can be updated.")

  val codeArtifactUpdateStatus =
    taskKey[Unit]("Updates the package status to Published.")
}

object CodeArtifactKeys extends CodeArtifactKeys

object InternalCodeArtifactKeys extends InternalCodeArtifactKeys
