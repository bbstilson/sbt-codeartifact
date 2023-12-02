package codeartifact

import utest._

object CodeArtifactRepoSpec extends TestSuite {
  val tests = Tests {
    test("fromUrl") {
      val raw =
        "https://com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com/maven/private"

      val repo = CodeArtifactRepo.fromUrl(raw)

      assert(repo.name == "private")
      assert(repo.domain == "com-example")
      assert(repo.host == "com-example-1234567890.d.codeartifact.us-west-2.amazonaws.com")
      assert(repo.owner == "1234567890")
      assert(repo.url == raw)
      assert(repo.realm == "com-example/private")
    }
  }

}
