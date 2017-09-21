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
  implicit val offerHatFormat = Json.format[OfferHat]
  implicit val offerclaimFormat = Json.format[OfferClaim]
  implicit val offerHatCredentialsFormat = Json.format[OfferHatCredentials]
  implicit val offerClaimsInfoFormat = Json.format[OfferClaimsInfo]

  implicit val noticeReads = Json.reads[Notice]
  implicit val noticeWrites = Json.writes[Notice]
  implicit val noticeFormat = Format(noticeReads, noticeWrites)
}

object DexJsonFormats extends DexJsonFormats
