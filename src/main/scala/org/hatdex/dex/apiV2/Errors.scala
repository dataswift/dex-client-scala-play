package org.hatdex.dex.apiV2

object Errors {
  class ApiException(
      message: String = "",
      cause: Throwable = None.orNull)
      extends Exception(message, cause)
  case class ForbiddenActionException(
      message: String = "",
      cause: Throwable = None.orNull)
      extends ApiException(message, cause)
  case class UnauthorizedActionException(
      message: String = "",
      cause: Throwable = None.orNull)
      extends ApiException(message, cause)
  case class DataFormatException(
      message: String = "",
      cause: Throwable = None.orNull)
      extends ApiException(message, cause)
  case class DetailsNotFoundException(
      message: String = "",
      cause: Throwable = None.orNull)
      extends ApiException(message, cause)
  case class BadRequestException(
      message: String = "",
      cause: Throwable = None.orNull)
      extends ApiException(message, cause)
}
