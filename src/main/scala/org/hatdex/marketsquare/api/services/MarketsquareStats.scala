/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 10 2016
 */

package org.hatdex.marketsquare.api.services

import org.hatdex.hat.api.models.DataStats
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait MarketsquareStats {

  import org.hatdex.hat.api.json.DataStatsFormat.dataStatsFormat

  val logger: Logger
  val ws: WSClient
  val schema: String
  val marketsquareAddress: String

  def postStats(access_token: String, stats: Seq[DataStats])(implicit ec: ExecutionContext): Future[Unit] = {
    val request: WSRequest = ws.url(s"$schema$marketsquareAddress/stats/report")
      .withVirtualHost(marketsquareAddress)
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

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
