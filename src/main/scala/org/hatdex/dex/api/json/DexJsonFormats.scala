/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.api.json

import org.hatdex.hat.api.json.HatJsonFormats
import org.hatdex.dex.api.models._
import play.api.libs.json._

trait DexJsonFormats extends HatJsonFormats {
  import play.api.libs.json.JodaWrites._
  import play.api.libs.json.JodaReads._

  implicit val offerHatFormat: OFormat[OfferHat]                       = Json.format[OfferHat]
  implicit val offerclaimFormat: OFormat[OfferClaim]                   = Json.format[OfferClaim]
  implicit val offerHatCredentialsFormat: OFormat[OfferHatCredentials] = Json.format[OfferHatCredentials]
  implicit val offerClaimsInfoFormat: OFormat[OfferClaimsInfo]         = Json.format[OfferClaimsInfo]

  implicit val noticeReads: Reads[Notice]    = Json.reads[Notice]
  implicit val noticeWrites: OWrites[Notice] = Json.writes[Notice]
  implicit val noticeFormat: Format[Notice]  = Format(noticeReads, noticeWrites)
}

object DexJsonFormats extends DexJsonFormats
