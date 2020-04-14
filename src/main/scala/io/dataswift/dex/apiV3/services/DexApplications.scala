package io.dataswift.dex.apiV3.services

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpRequest

import scala.concurrent.{ ExecutionContext, Future }
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

case class Person(name: String, favoriteNumber: Int)
trait PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val personFormat: RootJsonFormat[Person] = jsonFormat2(Person)
}

trait DexApplications extends PersonJsonSupport {
  this: HttpClient =>

  implicit val as: ActorSystem
  implicit val mat: Materializer
  implicit val ec: ExecutionContext

  protected val schema: String
  protected val dexAddress: String
  protected val apiVersion: String

  def applications(includeUnpublished: Boolean = false)(implicit ec: ExecutionContext): Future[Person] = {

    //    val url = Uri.from(
    //      scheme = schema,
    //      host = dexAddress,
    //      path = s"/api/$apiVersion/applications",
    //      queryString = Some(Query("unpublished" -> includeUnpublished.toString).toString)
    //    )

    val request = HttpRequest(uri = s"$schema$dexAddress/api/$apiVersion/applications")

    val eventualResponse = for {
      response <- sendRequest(request)
      payload <- Unmarshal(response.entity).to[Person]
    } yield payload

    eventualResponse
  }

}
