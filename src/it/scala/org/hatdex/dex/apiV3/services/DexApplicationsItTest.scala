package org.hatdex.dex.apiV3.services
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

//Requires a locally running Dex
class DexApplicationsItTest extends AsyncFunSuite with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val ec = scala.concurrent.ExecutionContext.global
  test("Get Applications") {
    play.api.test.WsTestClient.withClient { ws =>
      val client = new DexClient(ws, "localhost:9000", "http://")
      client.applications() map { _ mustBe a [Seq[_]] }
    }
  }

}
