package io.dataswift.dex.apiV3.services

import akka.http.scaladsl.model.{ HttpEntity, HttpResponse }
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._

import scala.concurrent.Future

class DexApplicationsSpec extends BaseSpec {
  "Dex Applications module" should {
    "return a list of available applications" in {
      val json =
        """
          | {
          |   "name": "Peter",
          |   "favoriteNumber": 2
          | }
          |""".stripMargin
      val response = Future.successful {
        HttpResponse(
          status = OK,
          entity = HttpEntity(`application/json`, json))
      }

      val client = new DexTestClient(response)

      client.applications(false).map { response =>
        response.name shouldBe "Peter"
        response.favoriteNumber shouldBe 2
      }
    }
  }
}
