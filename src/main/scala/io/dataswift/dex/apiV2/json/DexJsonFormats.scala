/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package io.dataswift.dex.apiV2.json

import io.dataswift.dex.apiV2.models.{ EndpointStructure, FieldInfo, FieldStructure, NamespaceStructure, Offer, OfferClaimSummary, OfferClaimsInfo, OfferHat, OfferHatCredentialsSummary, OfferRegistration }
import org.hatdex.hat.api.json.{ DataDebitFormats, RichDataJsonFormats }
import play.api.libs.functional.syntax._
import play.api.libs.json._

trait DexJsonFormats extends DataDebitFormats with RichDataJsonFormats {

  implicit val offervHatFormat = Json.format[OfferHat]
  implicit val offervClaimFormat = Json.format[OfferClaimSummary]
  implicit val offervHatCredentialsFormat = Json.format[OfferHatCredentialsSummary]
  implicit val offervClaimsInfoFormat = Json.format[OfferClaimsInfo]

  implicit val offerRegistrationFormat: Format[OfferRegistration] = Json.format[OfferRegistration]
  implicit val offerFormat: Format[Offer] = Json.format[Offer]

  implicit val fieldInfoFormat: Format[FieldInfo] = Json.format[FieldInfo]

  implicit val fieldStructureFormat: Format[FieldStructure] = (
    (__ \ "name").format[String] and
    (__ \ "fields").lazyFormatNullable(implicitly[Format[Seq[FieldStructure]]]) and
    (__ \ "description").formatNullable[String] and
    (__ \ "count").formatNullable[Long])(FieldStructure.apply, unlift(FieldStructure.unapply))

  implicit val endpointStructureFormat: Format[EndpointStructure] = Json.format[EndpointStructure]
  implicit val namespaceStructureFormat: Format[NamespaceStructure] = Json.format[NamespaceStructure]
}

object DexJsonFormats extends DexJsonFormats
