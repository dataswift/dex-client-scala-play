/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.api.services

import scala.concurrent.{ ExecutionContext, Future }

import io.dataswift.models.hat.DataStats
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

trait DexStats {

  import io.dataswift.models.hat.json.DataStatsFormat.dataStatsFormat

  val logger: Logger
  val ws: WSClient
  val schema: String
  val dexAddress: String

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

}
