/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 10 2016
 */

package org.hatdex.marketsquare.api.json

import org.hatdex.hat.api.json.HatJsonFormats
import org.hatdex.marketsquare.api.models._
import play.api.libs.json._

trait MarketsquareJsonFormats extends HatJsonFormats {
  implicit val offerHatFormat = Json.format[OfferHat]
  implicit val offerclaimFormat = Json.format[OfferClaim]
  implicit val offerHatCredentialsFormat = Json.format[OfferHatCredentials]
  implicit val offerClaimsInfoFormat = Json.format[OfferClaimsInfo]
}

object MarketsquareJsonFormats extends MarketsquareJsonFormats
