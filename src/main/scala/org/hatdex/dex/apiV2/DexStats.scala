package org.hatdex.dex.apiV2

import io.dataswift.models.dex.NamespaceStructure
import io.dataswift.models.dex.json.DexJsonFormats
import io.dataswift.models.hat.DataStats
import org.hatdex.dex.apiV2.Errors._
import play.api.Logger
import play.api.http.Status.OK
import play.api.libs.json.{ Format, Json }
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import scala.concurrent.{ ExecutionContext, Future }

trait DexStats {

  protected val logger: Logger
  protected val ws: WSClient
  protected val dexAddress: String
  protected val dexHost: String
  protected val apiVersion: String

  implicit protected val dataStatsFormat: Format[DataStats] =
    io.dataswift.models.hat.json.DataStatsFormat.dataStatsFormat

  implicit protected val namespaceStructureFormat: Format[NamespaceStructure] = DexJsonFormats.namespaceStructureFormat

  def postStats(
      access_token: String,
      stats: Seq[DataStats]
    )(implicit ec: ExecutionContext): Future[Unit] = {
    val request: WSRequest = ws
      .url(s"$dexAddress/stats/report")
      .withVirtualHost(dexHost)
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

  def availableData()(implicit ec: ExecutionContext): Future[Seq[NamespaceStructure]] = {
    val request: WSRequest = ws
      .url(s"$dexAddress/stats/available-data")
      .withVirtualHost(dexHost)
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Seq[NamespaceStructure]] recover {
                case e =>
                  val message = s"Error parsing namespace structures: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case _ =>
          val message = s"Available data collection failed: $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

}
