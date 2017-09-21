/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.api.services

import java.util.UUID

import org.hatdex.dex.api.json.DexJsonFormats
import org.hatdex.dex.api.models.Notice
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait DexDataPlugs {
  val logger: Logger
  val ws: WSClient
  val schema: String
  val dexAddress: String

  def dataplugConnectHat(access_token: String, dataplugId: UUID, hatAddress: String)(implicit ec: ExecutionContext): Future[Unit] = {
    logger.debug(s"Connect dataplug $dataplugId to $hatAddress via MarketSquare")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/dataplugs/$dataplugId/connect")
      .withVirtualHost(dexAddress)
      .withQueryString(("hat", hatAddress))
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
