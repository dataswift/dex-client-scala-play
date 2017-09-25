/*
 * Copyright (C) HAT Data Exchange Ltd - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Andrius Aucinas <andrius.aucinas@hatdex.org>, 8 2017
 */

package org.hatdex.dex.apiV2.models

case class FieldInfo(
  name: String,
  count: Long,
  description: Option[String])

case class FieldStructure(
  name: String,
  fields: Option[Seq[FieldStructure]],
  description: Option[String],
  count: Option[Long])

case class EndpointStructure(
  endpoint: String,
  fields: Seq[FieldStructure],
  description: Option[String])

case class NamespaceStructure(
  namespace: String,
  endpoints: Seq[EndpointStructure],
  description: Option[String])
