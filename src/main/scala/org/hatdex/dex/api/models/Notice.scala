/*
 * Copyright (C) 2016 HAT Data Exchange Ltd - All Rights Reserved
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 2 / 2017
 *
 */

package org.hatdex.dex.api.models

import org.joda.time.DateTime
import play.api.libs.json.Json
import play.api.libs.json.{ OWrites, Reads }

case class NoticeTarget(
    withAnyRole: Option[Seq[String]],
    withAllRoles: Option[Seq[String]],
    withEmail: Option[Seq[String]],
    withHatAddress: Option[Seq[String]])

object NoticeTarget {
  implicit val noticeTargetJsonReads: Reads[NoticeTarget] = Json.reads[NoticeTarget]
  implicit val noticeTargetJsonWrites: OWrites[NoticeTarget] = Json.writes[NoticeTarget]
}

case class Notice(
    id: Option[Long],
    message: String,
    dateCreated: DateTime,
    target: NoticeTarget)
