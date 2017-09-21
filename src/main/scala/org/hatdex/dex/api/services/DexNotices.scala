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
import org.hatdex.dex.api.models.{ Notice, OfferClaimsInfo }
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait DexNotices {
  val logger: Logger
  val ws: WSClient
  val schema: String
  val dexAddress: String

  import DexJsonFormats.noticeFormat

  def postNotice(access_token: String, notice: Notice)(implicit ec: ExecutionContext): Future[Notice] = {
    logger.debug(s"Post notice $notice to MarketSquare")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/notices")
      .withVirtualHost(dexAddress)
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
