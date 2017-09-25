/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV2.json

import org.hatdex.hat.api.json.{ DataDebitFormats, HatJsonFormats }
import org.hatdex.dex.apiV2.models._
import org.hatdex.hat.api.models.RichDataJsonFormats
import org.joda.time.Duration
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json._

trait DexJsonFormats extends DataDebitFormats with RichDataJsonFormats {

  /*
  * Duration Json formats
   */
  implicit val durationWrites: Writes[Duration] = new Writes[Duration] {
    def writes(o: Duration): JsValue = JsNumber(o.getMillis)
  }
  implicit val durationReads: Reads[Duration] = new Reads[Duration] {
    def reads(json: JsValue): JsResult[Duration] = json match {
      case JsNumber(value) => JsSuccess(new Duration(value.toLong))
      case _               => JsError(Seq(JsPath() -> Seq(ValidationError("validate.error.expected.period"))))
    }
  }

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
