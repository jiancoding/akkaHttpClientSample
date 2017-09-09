package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import client.IntraDayModel.IntraDayRequest
import client.{IntraDayProcessActor, TimeSeriesIntraDayActor}
import com.typesafe.config.Config
import helper.TestDBProvider
import mongoDB.IntraDayDao
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.duration._
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpecLike}

class TimeSeriesIntraDayActorTest
  extends TestKit(ActorSystem("testSystem"))
    with MustMatchers
  with TestDBProvider
    with WordSpecLike
    with MockitoSugar
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  val mockConfig = mock[Config]
  val mockIntraDay = mock[IntraDayDao]
  val intraDayProcessActor = TestActorRef(new IntraDayProcessActor(mockIntraDay, db))
  val testObject = TestActorRef(new TimeSeriesIntraDayActor(mockConfig, testActor))

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
  }

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "TimeSeriesIntraDayActorTest" must {
    "client call test" in {
      val request = IntraDayRequest("TIME_SERIES_INTRADAY", "MSFT", "1min", None, None, "26WTKJT35SAZF6PU")
      val result = testObject ! request
      val mess = testObject.underlyingActor.infoMessage
      mess mustBe "success"
      expectMsg(15 seconds, IntraDayResponse(null, List())) //pass after 4pm
    }

  }

}
