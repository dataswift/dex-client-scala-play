/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.marketsquare.api.services

import javax.inject.Inject

import play.api.libs.ws.WSClient

class MarketsquareClient(
  val ws: WSClient,
  val marketsquareAddress: String,
  override val schema: String) extends MarketsquareOffers
    with MarketsquareNotices
    with MarketsquareDataPlugs
    with MarketsquareStats {

  @Inject def this(ws: WSClient, marketsquareAddress: String) = this(ws, marketsquareAddress, "https://")

  val logger = play.api.Logger(this.getClass)
}
