package io.dataswift.dex.apiV3.services

import akka.actor.ActorSystem
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

abstract class BaseSpec extends AsyncWordSpec with Matchers {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
}
