/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV2.services

import org.hatdex.dex.apiV2.models.Application
import org.hatdex.dex.apiV2.services.Errors.{ ApiException, DataFormatException }
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Format
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait DexApplications {

  protected val logger: Logger
  protected val ws: WSClient
  protected val schema: String
  protected val dexAddress: String

  protected implicit val applicationFormat: Format[Application] = org.hatdex.dex.apiV2.json.ApplicationJsonProtocol.applicationFormat

  def applications()(implicit ec: ExecutionContext): Future[Seq[Application]] = {
    val request: WSRequest = ws.url(s"$schema$dexAddress/api/applications")
      .withVirtualHost(dexAddress)
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Seq[Application]] recover {
            case e =>
              val message = s"Error parsing application structures: $e"
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
