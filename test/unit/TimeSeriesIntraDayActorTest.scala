package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.IntraDayModel.IntraDayRequest
import client.TimeSeriesIntraDayActor
import org.scalatest.{MustMatchers, WordSpecLike}

class TimeSeriesIntraDayActorTest extends TestKit(ActorSystem("testSystem")) with MustMatchers with WordSpecLike {

  "TimeSeriesIntraDayActorTest" must {

    val testActor = TestActorRef[TimeSeriesIntraDayActor]

    "client call test" in {
      val request = IntraDayRequest("TIME_SERIES_INTRADAY", "MSFT", "1min", None, None, "26WTKJT35SAZF6PU")

      testActor ! request

    }
  }

}
