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
    val TestCommon = "0.2.3"
    val PlayJson   = "2.9.2"
    val DsBackend  = "2.3.0"
  }

  val resolvers = Seq(
    "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
  )

  object Library {
    object Play {
      val ws       = "com.typesafe.play" %% "play-ahc-ws"    % play.core.PlayVersion.current
      val json     = "com.typesafe.play" %% "play-json"      % Version.PlayJson
      val jsonJoda = "com.typesafe.play" %% "play-json-joda" % Version.PlayJson
    }

    val testCommon = "io.dataswift" %% "test-common" % Version.TestCommon

    object DataswiftModels {
      val hatPlay = "io.dataswift" %% "hat-play" % Version.DsBackend
      val dexPlay = "io.dataswift" %% "dex-play" % Version.DsBackend
    }

  }
}
