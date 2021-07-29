package org.hatdex.dex.apiV3

import akka.Done
import play.api.Logger
import play.api.http.Status.OK
import play.api.libs.json.Json
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

trait DexUsers {

  protected val logger: Logger
  protected val ws: WSClient
  protected val dexAddress: String
  protected val dexHost: String
  protected val apiVersion: String

  def registerHat(
      hatName: String,
      domain: String
    )(implicit ec: ExecutionContext): Future[Done] = {
    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/users/registerHat")
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
      applicationId: String
    )(implicit ec: ExecutionContext): Future[Done] = {
    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/users/register-consent/$applicationId")
      .withVirtualHost(dexHost)
      .withRequestTimeout(2500.millis)
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> accessToken)

    val futureResponse: Future[WSResponse] = request.get()
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
