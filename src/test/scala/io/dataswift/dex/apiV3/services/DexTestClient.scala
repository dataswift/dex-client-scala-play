package io.dataswift.dex.apiV3.services

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.Materializer

import scala.concurrent.{ ExecutionContext, Future }

trait TestClientHandler extends HttpClient {
  def response: Future[HttpResponse]

  override def sendRequest: HttpRequest => Future[HttpResponse] = (_: HttpRequest) => response
}

class DexTestClient(
    val response: Future[HttpResponse])(implicit val as: ActorSystem, val mat: Materializer, val ec: ExecutionContext)
  extends DexApplications
  with TestClientHandler {

  protected val schema: String = "http://"
  protected val dexAddress: String = "dex.hat.org"
  protected val apiVersion: String = "v1.1"
}
