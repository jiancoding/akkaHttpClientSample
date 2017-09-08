package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.IntraDayModel.IntraDayRequest
import client.{IntraDayProcessActor, TimeSeriesIntraDayActor}
import com.typesafe.config.Config
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpecLike}

class TimeSeriesIntraDayActorTest
  extends TestKit(ActorSystem("testSystem"))
    with MustMatchers
    with WordSpecLike
    with MockitoSugar
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  val mockConfig = mock[Config]
  val intraDayProcessActor = TestActorRef(new IntraDayProcessActor)
  //  val intraDayProcessActor2 = TestActorRef(new IntraDayProcessActor2)

  val testActor1 = TestActorRef(new TimeSeriesIntraDayActor(mockConfig, intraDayProcessActor))
  //  val testActor1 = TestActorRef(new TimeSeriesIntraDayActor(mockConfig, intraDayProcessActor2))

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
  }

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "TimeSeriesIntraDayActorTest" must {
    "client call test" in {
      val request = IntraDayRequest("TIME_SERIES_INTRADAY", "MSFT", "1min", None, None, "26WTKJT35SAZF6PU")
      val result = testActor1 ! request
      val mess = testActor1.underlyingActor.infoMessage
      mess mustBe "success"
    }


  }

}
