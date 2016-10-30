/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 10 2016
 */

package org.hatdex.marketsquare.api.models

import org.joda.time.DateTime
import play.api.libs.json.Json

case class NoticeTarget(
  withAnyRole: Option[Seq[String]],
  withAllRoles: Option[Seq[String]],
  withEmail: Option[Seq[String]],
  withHatAddress: Option[Seq[String]]
)

object NoticeTarget {
  implicit val noticeTargetJsonReads = Json.reads[NoticeTarget]
  implicit val noticeTargetJsonWrites = Json.writes[NoticeTarget]
}

case class Notice(id: Option[Long], message: String, dateCreated: DateTime, target: NoticeTarget)
