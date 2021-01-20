package org.hatdex.dex.apiV3

import scala.concurrent.ExecutionContext
import play.api.Logger
import akka.Done
import play.api.libs.ws.WSRequest
import play.api.libs.ws.WSResponse
import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import play.api.http.Status.OK

trait DexUsers {

  protected val logger: Logger
  protected val ws: WSClient
  protected val schema: String
  protected val dexAddress: String
  protected val apiVersion: String

  def registerHat(
      hatName: String,
      domain: String
    )(implicit ec: ExecutionContext): Future[Done] = {
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
      applicationId: String
    )(implicit ec: ExecutionContext): Future[Done] = {
    val request: WSRequest = ws
      .url(s"$schema$dexAddress/api/$apiVersion/users/register-consent/$applicationId")
      .withVirtualHost(dexAddress)
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
