package org.hatdex.dex.api

import io.dataswift.models.dex.Notice
import io.dataswift.models.dex.json.DexJsonFormats
import play.api.Logger
import play.api.http.Status.OK
import play.api.libs.json.Json
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import scala.concurrent.{ ExecutionContext, Future }

trait DexNotices {
  val logger: Logger
  val ws: WSClient
  val dexAddress: String

  import DexJsonFormats._

  def postNotice(
      access_token: String,
      notice: Notice
    )(implicit ec: ExecutionContext): Future[Notice] = {
    logger.debug(s"Post notice $notice to MarketSquare")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/notices")
      .withVirtualHost(dexAddress)
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.post(Json.toJson(notice))
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Notice] recover {
                case e =>
                  logger.error(s"Error parsing posted Notice: ${e}")
                  throw new RuntimeException(s"Error parsing posted Notice: ${e}")
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case _ =>
          logger.error(s"Posting notice $notice to MarketSquare failed, $response, ${response.body}")
          throw new RuntimeException(s"Posting notice $notice to MarketSquare failed, $response, ${response.body}")
      }
    }
  }

}
