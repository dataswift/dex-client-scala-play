/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

import sbt.Keys._
import sbt._

////*******************************
//// Basic settings
////*******************************
object BasicSettings extends AutoPlugin {
  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      organization := "org.hatdex",
      version := "3.1.0",
      resolvers ++= Dependencies.resolvers,
      scalaVersion := Dependencies.Versions.scalaVersion,
      crossScalaVersions := Dependencies.Versions.crossScala,
      name := "DEX Client Scala",
      description := "Dataswift Exchange HTTP API wrapper for Scala",
      licenses += ("Mozilla Public License 2.0", url("https://www.mozilla.org/en-US/MPL/2.0")),
      scmInfo := Some(
            ScmInfo(
              url("https://github.com/Hub-of-all-Things/dex-client-scala-play"),
              "scm:git@github.com:Hub-of-all-Things/dex-client-scala-play.git"
            )
          ),
      homepage := Some(url("https://dataswift.io")),
      developers := List(
            Developer(
              id = "AndriusA",
              name = "Andrius Aucinas",
              email = "andrius@smart-e.org",
              url = url("http://smart-e.org")
            ),
            Developer(
              id = "AugustinasM",
              name = "Augustinas Markevicius",
              email = "gus@codeandpicture.com",
              url = url("http://codeandpicture.com")
            )
          ),
      scalacOptions ++= Seq(
            "-deprecation", // Emit warning and location for usages of deprecated APIs.
            "-feature", // Emit warning and location for usages of features that should be imported explicitly.
            "-unchecked", // Enable additional warnings where generated code depends on assumptions.
            "-Xlint", // Enable recommended additional warnings.
            "-Ywarn-dead-code", // Warn when dead code is identified.
            "-language:postfixOps", // Allow postfix operators
            "-Ywarn-numeric-widen" // Warn when numerics are widened.
          ),
      scalacOptions in Test ~= { (options: Seq[String]) =>
        options filterNot (_ == "-Ywarn-dead-code") // Allow dead code in tests (to support using mockito).
      },
      parallelExecution in Test := false,
      fork in Test := true,
      // Needed to avoid https://github.com/travis-ci/travis-ci/issues/3775 in forked tests
      // in Travis with `sudo: false`.
      // See https://github.com/sbt/sbt/issues/653
      // and https://github.com/travis-ci/travis-ci/issues/3775
      javaOptions += "-Xmx1G"
    )
}
