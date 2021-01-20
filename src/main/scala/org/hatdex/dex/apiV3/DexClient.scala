/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.apiV3

import javax.inject.Inject
import org.hatdex.dex.api.DexDataPlugs
import org.hatdex.dex.api.DexNotices
import play.api.Logger
import play.api.libs.ws.WSClient

class DexClient(
    val ws: WSClient,
    val dexAddress: String,
    override val schema: String)
    extends DexOffers
    with DexNotices
    with DexDataPlugs
    with DexStats
    with DexUsers
    with DexApplications {

  override val apiVersion: String = "v3"
  @Inject def this(
      ws: WSClient,
      dexAddress: String) = this(ws, dexAddress, "https://")

  val logger: Logger = play.api.Logger(this.getClass)
}
