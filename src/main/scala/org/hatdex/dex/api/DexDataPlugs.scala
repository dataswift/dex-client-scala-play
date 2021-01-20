package org.hatdex.dex.api

import play.api.http.Status.OK
import play.api.libs.ws.WSResponse

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSRequest
import java.util.UUID

import play.api.Logger

trait DexDataPlugs {
  val logger: Logger
  val ws: WSClient
  val schema: String
  val dexAddress: String

  def dataplugConnectHat(
      access_token: String,
      dataplugId: UUID,
      hatAddress: String
    )(implicit ec: ExecutionContext): Future[Unit] = {
    logger.debug(s"Connect dataplug $dataplugId to $hatAddress via MarketSquare")

    val request: WSRequest = ws
      .url(s"$schema$dexAddress/api/dataplugs/$dataplugId/connect")
      .withVirtualHost(dexAddress)
      .withQueryStringParameters(("hat", hatAddress))
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          ()
        case _ =>
          val message =
            s"Connecting dataplug $dataplugId to $hatAddress via MarketSquare failed: $response, ${response.body}"
          logger.error(message)
          throw new RuntimeException(message)
      }
    }
  }

}
