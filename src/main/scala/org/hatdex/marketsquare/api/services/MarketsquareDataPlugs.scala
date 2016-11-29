/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 10 2016
 */

package org.hatdex.marketsquare.api.services

import java.util.UUID

import org.hatdex.marketsquare.api.json.MarketsquareJsonFormats
import org.hatdex.marketsquare.api.models.Notice
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait MarketsquareDataPlugs {
  val logger: Logger
  val ws: WSClient
  val schema: String
  val marketsquareAddress: String

  def dataplugConnectHat(access_token: String, dataplugId: UUID, hatAddress: String)(implicit ec: ExecutionContext): Future[Unit] = {
    logger.debug(s"Connect dataplug $dataplugId to $hatAddress via MarketSquare")

    val request: WSRequest = ws.url(s"$schema$marketsquareAddress/api/dataplugs/$dataplugId/connect")
      .withVirtualHost(marketsquareAddress)
      .withQueryString(("hat", "mike.hubofallthings.net"))
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          ()
        case _ =>
          val message = s"Connecting dataplug $dataplugId to $hatAddress via MarketSquare failed: $response, ${response.body}"
          logger.error(message)
          throw new RuntimeException(message)
      }
    }
  }

}
