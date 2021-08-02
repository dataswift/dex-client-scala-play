package org.hatdex.dex.apiV2

import akka.Done
import io.dataswift.models.hat.applications.{ Application, ApplicationDeveloper, ApplicationHistory }
import org.hatdex.dex.apiV2.Errors._
import play.api.Logger
import play.api.http.Status.{ CREATED, FORBIDDEN, NOT_FOUND, OK, UNAUTHORIZED }
import play.api.libs.json.{ Format, JsError, JsSuccess, Json }
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }

import scala.concurrent.{ ExecutionContext, Future }

trait DexApplications {

  protected val logger: Logger
  protected val ws: WSClient
  protected val dexAddress: String
  protected val apiVersion: String

  implicit protected val applicationFormat: Format[Application] =
    io.dataswift.models.hat.json.ApplicationJsonProtocol.applicationFormat
  implicit protected val applicationHistoryFormat: Format[ApplicationHistory] =
    io.dataswift.models.hat.json.ApplicationJsonProtocol.applicationHistoryFormat
  implicit protected val developerFormat: Format[ApplicationDeveloper] =
    io.dataswift.models.hat.json.ApplicationJsonProtocol.applicationDeveloperFormat

  def applications(includeUnpublished: Boolean = false)(implicit ec: ExecutionContext): Future[Seq[Application]] = {
    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications")
      .withQueryStringParameters("unpublished" -> includeUnpublished.toString)
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Seq[Application]] recover {
                case e =>
                  val message = s"Error parsing application structures: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case _ =>
          val message = s"Retrieving application info failed: $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

  def application(
      applicationId: String,
      lang: Option[String] = None
    )(implicit ec: ExecutionContext): Future[Application] = {
    val requestedLanguage = lang.getOrElse("en")
    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/$applicationId")
      .withQueryStringParameters("lang" -> requestedLanguage)
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.flatMap { response =>
      response.status match {
        case OK =>
          response.json.validate[Application] match {
            case s: JsSuccess[Application] => Future.successful(s.get)
            case e: JsError =>
              val message = s"Error parsing application structures: $e"
              logger.error(message)
              Future.failed(DataFormatException(message))
          }

        case NOT_FOUND =>
          val message = s"[$applicationId] [$requestedLanguage] Application information not found."
          logger.info(message)
          Future.failed(DetailsNotFoundException(message))
        case _ =>
          val message = s"Retrieving application info failed: $response, ${response.body}"
          logger.error(message)
          Future.failed(new ApiException(s"[$applicationId] [$requestedLanguage] Failed to verify application ID"))
      }
    }
  }

  def applicationHistory(
      includeUnpublished: Boolean = false
    )(implicit ec: ExecutionContext): Future[Seq[ApplicationHistory]] = {
    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications-history")
      .withQueryStringParameters("unpublished" -> includeUnpublished.toString)
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Seq[ApplicationHistory]] recover {
                case e =>
                  val message = s"Error parsing application structures: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case _ =>
          val message = s"Retrieving application history failed: $response, ${response.body}"
          logger.error(message)
          throw new ApiException(message)
      }
    }
  }

  def registerApplication(
      access_token: String,
      application: Application
    )(implicit ec: ExecutionContext): Future[Application] = {
    logger.debug(s"Register new app with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications")
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.post(Json.toJson(application))
    futureResponse.map { response =>
      response.status match {
        case CREATED =>
          val jsResponse = response.json.validate[Application] recover {
                case e =>
                  val message = s"Error parsing application: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Registering application with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Registering application with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while registering application with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def editApplication(
      access_token: String,
      application: Application
    )(implicit ec: ExecutionContext): Future[Application] = {
    logger.debug(s"Editing app with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/${application.id}")
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.put(Json.toJson(application))
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Application] recover {
                case e =>
                  val message = s"Error parsing application: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Editing application with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Editing application with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while editing application with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def publishApplication(
      access_token: String,
      application: Application
    )(implicit ec: ExecutionContext): Future[Done] = {
    logger.debug(s"Publishing app with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/${application.id}/publish")
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          Done
        case UNAUTHORIZED =>
          val message = s"Publishing application with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Publishing application with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while publishing application with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def suspendApplication(
      access_token: String,
      application: Application
    )(implicit ec: ExecutionContext): Future[Done] = {
    logger.debug(s"Suspending app with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/${application.id}/suspend")
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          Done
        case UNAUTHORIZED =>
          val message = s"Suspending application with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Suspending application with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while suspending application with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def fetchApplication(applicationId: String)(implicit ec: ExecutionContext): Future[Application] = {
    logger.debug(s"Fetching application $applicationId with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/$applicationId")
      .withHttpHeaders("Accept" -> "application/json")

    val futureResponse: Future[WSResponse] = request.get()
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[Application] recover {
                case e =>
                  val message = s"Error parsing application: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Fetching application with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Fetching application with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while fetching application with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def updateDeveloper(
      access_token: String,
      developer: ApplicationDeveloper
    )(implicit ec: ExecutionContext): Future[ApplicationDeveloper] = {
    logger.debug(s"Updating developer with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/developer")
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.put(Json.toJson(developer))
    futureResponse.map { response =>
      response.status match {
        case OK =>
          val jsResponse = response.json.validate[ApplicationDeveloper] recover {
                case e =>
                  val message = s"Error parsing developer: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Updating developer with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Updating developer with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message = s"Unexpected error while updating developer with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }

  def createNewAppVersion(
      access_token: String,
      application: Application
    )(implicit ec: ExecutionContext): Future[Application] = {
    logger.debug(s"Creating new app version with $dexAddress")

    val request: WSRequest = ws
      .url(s"$dexAddress/api/$apiVersion/applications/${application.id}/versions")
      .withHttpHeaders("Accept" -> "application/json", "X-Auth-Token" -> access_token)

    val futureResponse: Future[WSResponse] = request.post(Json.toJson(application))
    futureResponse.map { response =>
      response.status match {
        case CREATED =>
          val jsResponse = response.json.validate[Application] recover {
                case e =>
                  val message = s"Error parsing application: $e"
                  logger.error(message)
                  throw DataFormatException(message)
              }
          // Convert to OfferClaimsInfo - if validation has failed, it will have thrown an error already
          jsResponse.get
        case UNAUTHORIZED =>
          val message = s"Creating new application version with $dexAddress unauthorized"
          logger.error(message)
          throw UnauthorizedActionException(message)
        case FORBIDDEN =>
          val message = s"Creating new application version with $dexAddress forbidden - necessary permissions not found"
          logger.error(message)
          throw ForbiddenActionException(message)
        case _ =>
          val message =
            s"Unexpected error while creating new application version with $dexAddress: $response, ${response.body}"
          throw new ApiException(message)
      }
    }
  }
}
