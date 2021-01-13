/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV3.services

import io.dataswift.models.dex.NamespaceStructure
import io.dataswift.models.dex.play.DexJsonFormats
import org.hatdex.dex.apiV3.services.Errors.{ ApiException, DataFormatException }
import io.dataswift.models.hat.DataStats
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }
import play.api.libs.json.Format

trait DexStats {

  protected val logger: Logger
  protected val ws: WSClient
  protected val schema: String
  protected val dexAddress: String
  protected val apiVersion: String

  implicit protected val dataStatsFormat: Format[DataStats]                   = io.dataswift.models.hat.json.DataStatsFormat.dataStatsFormat
  implicit protected val namespaceStructureFormat: Format[NamespaceStructure] = DexJsonFormats.namespaceStructureFormat

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

  def availableData()(implicit ec: ExecutionContext): Future[Seq[NamespaceStructure]] = {
    val request: WSRequest = ws
      .url(s"$schema$dexAddress/stats/available-data")
      .withVirtualHost(dexAddress)
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
