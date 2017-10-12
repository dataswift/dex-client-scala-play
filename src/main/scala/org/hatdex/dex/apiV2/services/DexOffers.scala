/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV2.services

import org.hatdex.dex.apiV2.json.DexJsonFormats
import org.hatdex.dex.apiV2.models.{ Offer, OfferClaimSummary, OfferClaimsInfo, OfferRegistration }
import org.hatdex.dex.apiV2.services.Errors._
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws._

import scala.concurrent.{ ExecutionContext, Future }

trait DexOffers {
  val logger: Logger
  val ws: WSClient
  val schema: String
  val dexAddress: String

  import DexJsonFormats._

  def listOffers()(implicit ec: ExecutionContext): Future[Seq[Offer]] = {
    logger.debug(s"Get DEX data offers from $dexAddress")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/v2/offers")
      .withVirtualHost(dexAddress)
      .withHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Seq[Offer]] recover {
            case e =>
              val message = s"Error parsing successful offer list response: $e"
              logger.error(message)
              throw DataFormatException(message)
          }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case _ =>
          val message = s"Fetching offers from $dexAddress failed, $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

  def registerOffer(access_token: String, offer: OfferRegistration)(implicit ec: ExecutionContext): Future[Offer] = {
    logger.debug(s"Register new offer with $dexAddress")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/v2/offer")
      .withVirtualHost(dexAddress)
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.post(Json.toJson(offer))
    futureResponse.map { response =>
      response.status match {
        case CREATED =>
          val jsResponse = response.json.validate[Offer] recover {
            case e =>
              val message = s"Error parsing offer: $e"
              logger.error(message)
              throw DataFormatException(message)
          }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Registering offer with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Registering offer with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while registering offer with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def offerClaims(access_token: String, offerId: String)(implicit ec: ExecutionContext): Future[OfferClaimsInfo] = {
    logger.debug(s"Get Data Debit $offerId values from $dexAddress")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/v2/offer/$offerId/claims")
      .withVirtualHost(dexAddress)
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[OfferClaimsInfo] recover {
            case e =>
              val message = s"Error parsing successful offer claims info response: $e"
              logger.error(message)
              throw DataFormatException(message)
          }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Fetching Offer $offerId claims from $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Fetching Offer $offerId claims from $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Fetching Offer $offerId claims from $dexAddress failed, $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

  def registerOfferClaim(access_token: String, offerId: String, hat: String)(implicit ec: ExecutionContext): Future[OfferClaimSummary] = {
    logger.debug(s"Get Data Debit $offerId values from $dexAddress")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/v2/offer/$offerId/registerClaim")
      .withVirtualHost(dexAddress)
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)
      .withQueryString(("hat", hat))

    val futureResponse: Future[WSResponse] = request.put("")
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[OfferClaimSummary] recover {
            case e =>
              val message = s"Error parsing successful offer $offerId claim by $hat response: $e"
              logger.error(message)
              throw DataFormatException(message)
          }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Registering offer $offerId claim by $hat with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Registering offer $offerId claim by $hat with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case NOT_FOUND =>
          val message = (response.json \ "message").asOpt[String].getOrElse("") +
            (response.json \ "cause").asOpt[String].map(c => s": $c")
          logger.error(message)
          throw DetailsNotFoundException(message)
        case BAD_REQUEST =>
          val message = (response.json \ "message").asOpt[String].getOrElse("") +
            (response.json \ "cause").asOpt[String].map(c => s": $c")
          logger.error(message)
          throw BadRequestException(message)
        case _ =>
          val message = s"Registering offer $offerId claim by $hat with $dexAddress failed, $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

  def updateOfferStatus(access_token: String, offerId: String, status: String)(implicit ec: ExecutionContext): Future[Offer] = {
    logger.debug(s"Get Data Debit $offerId values from $dexAddress")

    val request: WSRequest = ws.url(s"$schema$dexAddress/api/v2/offer/$offerId")
      .withVirtualHost(dexAddress)
      .withHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)
      .withQueryString(("status", status))

    val futureResponse: Future[WSResponse] = request.put("")
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Offer] recover {
            case e =>
              val message = s"Error parsing successful offer $offerId status update response: $e"
              logger.error(message)
              throw DataFormatException(message)
          }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Updating offer $offerId status unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Updating offer $offerId status forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case NOT_FOUND =>
          val message = (response.json \ "message").asOpt[String].getOrElse("") +
            (response.json \ "cause").asOpt[String].map(c => s": $c")
          logger.error(message)
          throw DetailsNotFoundException(message)
        case BAD_REQUEST =>
          val message = (response.json \ "message").asOpt[String].getOrElse("") +
            (response.json \ "cause").asOpt[String].map(c => s": $c")
          logger.error(message)
          throw BadRequestException(message)
        case _ =>
          val message = s"Updating offer $offerId status failed, $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

}
