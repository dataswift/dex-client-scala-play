/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.api

import play.api.Logger
import play.api.libs.ws.WSClient

class DexClient(
    val ws: WSClient,
    val dexAddress: String)
    extends DexOffers
    with DexNotices
    with DexDataPlugs
    with DexStats {

  val logger: Logger = play.api.Logger(this.getClass)
}
