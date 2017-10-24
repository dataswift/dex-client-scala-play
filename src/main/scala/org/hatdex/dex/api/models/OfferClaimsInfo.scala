/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.api.models

import java.util.UUID

import org.joda.time.DateTime

case class OfferHat(
    address: String,
    publicKey: String)

case class OfferClaim(
    offerId: UUID,
    user: OfferHat,
    relationship: String,
    confirmed: Boolean,
    dataDebitId: Option[UUID],
    dateCreated: DateTime,
    claimerNumber: Option[Int])

case class OfferHatCredentials(
    offerId: UUID,
    login: String,
    passwordPlain: String,
    passwordHash: String,
    dateCreated: DateTime)

case class OfferClaimsInfo(
    credentials: OfferHatCredentials,
    claims: Seq[OfferClaim])
