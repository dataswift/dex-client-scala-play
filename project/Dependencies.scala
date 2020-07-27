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
    val crossScala = Seq("2.12.11")
    val scalaVersion = crossScala.head
  }

  val resolvers = Seq(
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "HAT Library Artifacts Snapshots" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-snapshots.hubofallthings.com",
    "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com",
    Resolver.file("local-environment", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)
  )

  object Library {
    object Play {
      val version = play.core.PlayVersion.current
      val ws = "com.typesafe.play" %% "play-ahc-ws" % version
      val cache = "com.typesafe.play" %% "play-cache" % version
      val json = "com.typesafe.play" %% "play-json" % "2.6.9"
      val jsonJoda = "com.typesafe.play" %% "play-json-joda" % "2.6.9"
    }

    object Specs2 {
      private val version = "3.9.5"
      val core = "org.specs2" %% "specs2-core" % version
      val matcherExtra = "org.specs2" %% "specs2-matcher-extra" % version
      val mock = "org.specs2" %% "specs2-mock" % version
    }

    object HATDeX {
      private val version = "2.6.13"
      val hatClient = "org.hatdex" %% "hat-client-scala-play" % version
    }

    object Test{
      val scalacheck = "org.scalacheck"        %% "scalacheck"           % "1.14.3" % "it,test"
      val scalatest = "org.scalatest"         %% "scalatest"            % "3.2.0" % "it,test"
      val funsuite = "org.scalatest" %% "scalatest-funsuite" % "3.2.0" % "it,test"
      val testpluscheck = "org.scalatestplus" %% "scalacheck-1-14" % "3.2.0.0" % "it,test"
      val matchers = "org.scalatest" %% "scalatest-mustmatchers" % "3.2.0" % "it,test"
      val logging = "ch.qos.logback" % "logback-classic" % "1.2.3" % "it,test"
    }
  }
}
