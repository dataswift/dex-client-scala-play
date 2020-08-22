/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV3.services

import akka.Done
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

trait DexUsers {

  protected val logger: Logger
  protected val ws: WSClient
  protected val schema: String
  protected val dexAddress: String
  protected val apiVersion: String

  def registerHat(
    hatName: String,
    domain: String)(implicit ec: ExecutionContext): Future[Done] = {
    val request: WSRequest = ws
      .url(s"$schema$dexAddress/api/$apiVersion/users/registerHat")
      .withHttpHeaders("Accept" -> "application/json")

    val hat = Json.obj("hatAddress" -> Json.toJson(s"$hatName.$domain"))

    val futureResponse: Future[WSResponse] = request.post(hat)
    futureResponse.map { response =>
      logger.debug(s"Register Hat Response: $response")
      response.status match {
        case OK => Done
        case _  => handleErrorResponses(response)
      }
    }
  }

  def registerTosConsent(
    accessToken: String,
    applicationId: String)(implicit ec: ExecutionContext): Future[Done] = {
    val request: WSRequest = ws
      .url(s"$schema$dexAddress/api/$apiVersion/users/register-consent/$applicationId")
      .withVirtualHost(dexAddress)
      .withRequestTimeout(2500.millis)
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> accessToken)

    val futureResponse: Future[WSResponse] = request.get
    futureResponse.map { response =>
      logger.debug(s"Registering user's consent to TOS: $response")
      response.status match {
        case OK => Done
        case _  => handleErrorResponses(response)
      }
    }
  }

  private def handleErrorResponses(response: WSResponse): Nothing = {
    val message = (response.json \ "message")
      .validate[String]
      .getOrElse("Unknown error occurred")

    val error = (response.json \ "cause")
      .validate[String]
      .getOrElse("")
    throw new RuntimeException(s"$message - $error")
  }
}
