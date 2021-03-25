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

  object Versions {
    val crossScala   = Seq("2.13.5", "2.12.13")
    val scalaVersion = crossScala.head
    val testCommon   = "0.2.3"
  }

  val resolvers = Seq(
    "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
  )

  object Library {
    object Play {
      val version  = play.core.PlayVersion.current
      val ws       = "com.typesafe.play" %% "play-ahc-ws"    % version
      val json     = "com.typesafe.play" %% "play-json"      % "2.9.1"
      val jsonJoda = "com.typesafe.play" %% "play-json-joda" % "2.9.1"
    }

    val testCommon = "io.dataswift" %% "test-common" % Versions.testCommon

    object DataswiftModels {
      private val version = "2.2.0"
      val hatPlay         = "io.dataswift" %% "hat-play" % version
      val dexPlay         = "io.dataswift" %% "dex-play" % version
    }

  }
}
