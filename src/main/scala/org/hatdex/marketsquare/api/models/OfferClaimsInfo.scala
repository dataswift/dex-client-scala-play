/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 10 2016
 */

package org.hatdex.marketsquare.api.models

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
