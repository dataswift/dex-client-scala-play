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
    val crossScala = Seq("2.12.11") // "2.13.1"
    val scalaVersion = crossScala.head
  }

  lazy val akkaVersion = "2.6.4"
  lazy val akkaHttpVersion = "10.1.11"
  lazy val logbackVersion = "1.2.3"
  lazy val scalaTestVersion = "3.1.0"

  val resolvers = Seq(
    "Atlassian Releases" at "https://maven.atlassian.com/public/",
    "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
    "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "HAT Library Artifacts Snapshots" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-snapshots.hubofallthings.com",
    "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
  )

  object Library {
    object Play {
      val version = play.core.PlayVersion.current
      val ws = "com.typesafe.play" %% "play-ahc-ws" % version
      val cache = "com.typesafe.play" %% "play-cache" % version
//      val test = "com.typesafe.play" %% "play-test" % version
//      val specs2 = "com.typesafe.play" %% "play-specs2" % version
      val json = "com.typesafe.play" %% "play-json" % "2.8.1"
      val jsonJoda = "com.typesafe.play" %% "play-json-joda" % "2.8.1"
    }

    object Specs2 {
      private val version = "4.8.3"
      val core = "org.specs2" %% "specs2-core" % version
      val matcherExtra = "org.specs2" %% "specs2-matcher-extra" % version
      val mock = "org.specs2" %% "specs2-mock" % version
    }

    object Akka {
      val http = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
      val sprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
      val stream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
      val typedActor = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
    }

    object Logging {
      val akkaLogger = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
      val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
    }

    object Test {
      val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
      val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
      val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
    }

    object HATDeX {
      private val version = "2.6.8-SNAPSHOT"
      val hatClient = "org.hatdex" %% "hat-client-scala-play" % version
    }
  }
}
