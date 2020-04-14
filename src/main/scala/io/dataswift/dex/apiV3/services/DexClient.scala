package io.dataswift.dex.apiV3.services

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.Materializer

import scala.concurrent.{ ExecutionContext, Future }

trait HttpClient {
  def sendRequest: HttpRequest => Future[HttpResponse]
}

trait ClientHandler extends HttpClient {
  implicit def as: ActorSystem

  override def sendRequest: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)
}

class DexClient(
    secure: Boolean = true,
    val dexAddress: String,
    val apiVersion: String = "v1.1")(implicit val as: ActorSystem, val mat: Materializer, val ec: ExecutionContext)
  extends DexApplications
  with ClientHandler {

  protected val schema: String = if (secure) "https://" else "http://"
}
