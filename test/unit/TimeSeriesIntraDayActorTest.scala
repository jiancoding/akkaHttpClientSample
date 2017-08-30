package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.IntraDayModel.IntraDayRequest
import client.TimeSeriesIntraDayActor
import com.typesafe.config.Config
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, MustMatchers, WordSpecLike}

class TimeSeriesIntraDayActorTest
  extends TestKit(ActorSystem("testSystem"))
    with MustMatchers
    with WordSpecLike
    with MockitoSugar
    with BeforeAndAfterEach {

  val mockConfig = mock[Config]

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
  }

  "TimeSeriesIntraDayActorTest" must {


    val testActor = TestActorRef(new TimeSeriesIntraDayActor(mockConfig))

    "client call test" in {
      val request = IntraDayRequest("TIME_SERIES_INTRADAY", "MSFT", "1min", None, None, "26WTKJT35SAZF6PU")
      testActor ! request
      val result = testActor.underlyingActor.abc
      result mustBe "success"

    }



  }

}
