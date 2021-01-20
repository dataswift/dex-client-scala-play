package org.hatdex.dex.apiV3.services
import org.hatdex.dex.apiV2.Errors.DetailsNotFoundException
import io.dataswift.models.hat.applications.ApplicationKind
import org.hatdex.dex.apiV3.DexClient
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

//Requires a locally running Dex
class DexApplicationsItTest extends AsyncFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val ec = scala.concurrent.ExecutionContext.global
  test("Get All Applications") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "localhost:9000", "http://")
      client.applications() map { _ mustBe a [Seq[_]] }
    }
  }

  test("Get All Applications with filters and pagination") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "localhost:9000", "http://")
      client.applications(unpublished = Some(true), kind = Some(ApplicationKind.DataPlug("Blah")), startId = None, limit = Some(5)) map { _ mustBe a [Seq[_]] }
    }
  }

  test("Get Application By Id") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "localhost:9000", "http://")
      recoverToSucceededIf[DetailsNotFoundException] {
        client.application("Some Id") map { _ mustBe a [Seq[_]] }
      }
    }
  }

}
