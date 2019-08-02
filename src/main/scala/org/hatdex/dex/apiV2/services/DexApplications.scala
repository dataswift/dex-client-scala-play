/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV2.services

import org.hatdex.dex.apiV2.services.Errors.{ ApiException, DataFormatException }
import org.hatdex.hat.api.models.applications.{ Application, ApplicationHistory }
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
  protected val apiVersion: String

  protected implicit val applicationFormat: Format[Application] = org.hatdex.hat.api.json.ApplicationJsonProtocol.applicationFormat
  protected implicit val applicationHistoryFormat: Format[ApplicationHistory] = org.hatdex.hat.api.json.ApplicationJsonProtocol.applicationHistoryFormat

  def applications(includeUnpublished: Boolean = false)(implicit ec: ExecutionContext): Future[Seq[Application]] = {
    val request: WSRequest = ws.url(s"$schema$dexAddress/api/$apiVersion/applications")
      .withVirtualHost(dexAddress)
      .withQueryStringParameters("unpublished" -> includeUnpublished.toString)
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
          val message = s"Retrieving application info failed: $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

  def applicationHistory(includeUnpublished: Boolean = false)(implicit ec: ExecutionContext): Future[Seq[ApplicationHistory]] = {
    val request: WSRequest = ws.url(s"$schema$dexAddress/api/$apiVersion/applications-history")
      .withVirtualHost(dexAddress)
      .withQueryStringParameters("unpublished" -> includeUnpublished.toString)
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Seq[ApplicationHistory]] recover {
            case e =>
              val message = s"Error parsing application structures: $e"
              logger.error(message)
              throw DataFormatException(message)
          }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case _ =>
          val message = s"Retrieving application history failed: $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

}
