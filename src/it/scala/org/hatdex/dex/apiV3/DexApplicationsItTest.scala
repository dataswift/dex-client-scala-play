package org.hatdex.dex.apiV3

import org.hatdex.dex.apiV2.Errors.DetailsNotFoundException
import io.dataswift.models.hat.applications.ApplicationKind
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.must.Matchers

//Requires a locally running Dex
class DexApplicationsItTest extends AsyncFunSuite with Matchers {

  implicit val ec = scala.concurrent.ExecutionContext.global
  test("Get All Applications") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "http://localhost:9000")
      client.applications() map { _ mustBe a [Seq[_]] }
    }
  }

  test("Get All Applications with filters and pagination") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "http://localhost:9000")
      client.applications(unpublished = Some(true), kind = Some(ApplicationKind.DataPlug("Blah")), startId = None, limit = Some(5)) map { _ mustBe a [Seq[_]] }
    }
  }

  test("Get Application By Id") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "http://localhost:9000")
      recoverToSucceededIf[DetailsNotFoundException] {
        client.application("Some Id") map { _ mustBe a [Seq[_]] }
      }
    }
  }

}
