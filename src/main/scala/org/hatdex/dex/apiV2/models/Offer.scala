package org.hatdex.dex.apiV2.models

import java.util.UUID

import org.hatdex.hat.api.models.EndpointDataBundle
import org.joda.time.{ DateTime, Duration }

case class Offer(
    offerId: String,
    created: DateTime,
    provider: UUID,
    title: String,
    description: String,
    starts: DateTime,
    expires: DateTime,
    collectionDuration: Option[Duration],
    dataConditions: Option[EndpointDataBundle],
    requiredData: EndpointDataBundle,
    requiredMinUser: Long,
    requiredMaxUser: Long,
    status: String // status one of "draft", "paid", "approved", "rejected", "oversubscribed", "satisfied"
) {

  def approved: Boolean = {
    status == "approved" || status == "satisfied" || status == "oversubscribed"
  }

  def satisfied: Boolean = {
    status == "satisfied" || status == "oversubscribed"
  }
}

object Offer {
  def apply(registration: OfferRegistration, providerId: UUID): Offer = {
    Offer(
      registration.offerId,
      registration.created.getOrElse(DateTime.now()), providerId,
      registration.title, registration.description,
      registration.starts, registration.expires,
      registration.collectionDuration,
      registration.dataConditions, registration.requiredData,
      registration.requiredMinUser, registration.requiredMaxUser,
      registration.status)
  }
}

case class OfferRegistration(
    offerId: String,
    created: Option[DateTime],
    title: String,
    description: String,
    starts: DateTime,
    expires: DateTime,
    collectionDuration: Option[Duration],
    dataConditions: Option[EndpointDataBundle],
    requiredData: EndpointDataBundle,
    requiredMinUser: Long,
    requiredMaxUser: Long,
    status: String)
