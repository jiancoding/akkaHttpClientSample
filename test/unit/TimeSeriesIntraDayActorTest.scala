package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.TimeSeriesIntraDayActor
import com.typesafe.config.Config
import helper.TestDBProvider
import model.IntraDayData
import model.IntraDayModel.IntraDayRequest
import mongoDB.DBActor
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpecLike}

import scala.concurrent.duration._

class TimeSeriesIntraDayActorTest
  extends TestKit(ActorSystem("testSystem"))
    with MustMatchers
    with TestDBProvider
    with WordSpecLike
    with MockitoSugar
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  val mockConfig = mock[Config]
  val mockIntraDay = mock[DBActor]
  val testObject = TestActorRef(new TimeSeriesIntraDayActor(mockConfig, testActor))

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
  }

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "TimeSeriesIntraDayActorTest" must {
    "client call test" in {
      val request = IntraDayRequest("TIME_SERIES_INTRADAY", "MSFT", "1min", None, None, "26WTKJT35SAZF6PU")
      testObject ! request
      expectMsg(15 seconds, (IntraDayData(null, List()), "intraDay")) //pass after 4pm
    }

  }

}
