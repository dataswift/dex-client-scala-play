package org.hatdex.dex.api

import scala.concurrent.{ ExecutionContext, Future }

import io.dataswift.models.hat.DataStats
import play.api.Logger
import play.api.http.Status.OK
import play.api.libs.json.Json
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

trait DexStats {

  import io.dataswift.models.hat.json.DataStatsFormat.dataStatsFormat

  val logger: Logger
  val ws: WSClient
  val schema: String
  val dexAddress: String

  def postStats(
      access_token: String,
      stats: Seq[DataStats]
    )(implicit ec: ExecutionContext): Future[Unit] = {
    val request: WSRequest = ws
      .url(s"$schema$dexAddress/stats/report")
      .withVirtualHost(dexAddress)
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.post(Json.toJson(stats))
    futureResponse.map { response =>
      response.status match {
        case OK =>
          Future.successful(())
        case _ =>
          logger.error(s"Data Stats reporting failed: $response, ${response.body}")
          Future.failed(new RuntimeException(s"Data Stats reporting failed: $response, ${response.body}"))
      }
    }
  }

}
