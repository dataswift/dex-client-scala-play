/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

import sbt._

object Dependencies {

  object Version {
    val DsBackend = "2.5.6"
  }

  val resolvers = Seq(
    "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
  )

  object Library {
    val TestCommon = "io.dataswift" %% "test-common" % Version.DsBackend
    val HatPlay    = "io.dataswift" %% "hat-play"    % Version.DsBackend
    val DexPlay    = "io.dataswift" %% "dex-play"    % Version.DsBackend
  }
}
