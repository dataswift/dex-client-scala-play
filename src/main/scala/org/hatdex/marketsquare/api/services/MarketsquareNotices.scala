/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 10 2016
 */

package org.hatdex.marketsquare.api.services

import java.util.UUID

import org.hatdex.marketsquare.api.json.MarketsquareJsonFormats
import org.hatdex.marketsquare.api.models.{ Notice, OfferClaimsInfo }
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait MarketsquareNotices {
  val logger: Logger
  val ws: WSClient
  val schema: String
  val marketsquareAddress: String

  import MarketsquareJsonFormats.noticeFormat

  def postNotice(access_token: String, notice: Notice)(implicit ec: ExecutionContext): Future[Notice] = {
    logger.debug(s"Post notice $notice to MarketSquare")

    val request: WSRequest = ws.url(s"$schema$marketsquareAddress/api/notices")
      .withVirtualHost(marketsquareAddress)
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

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
