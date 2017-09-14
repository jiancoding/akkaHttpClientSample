package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.TimeSeriesDailyActor
import com.typesafe.config.Config
import helper.TestDBProvider
import model.StockRequest.DailyStockRequest
import model.{DbRequest, IntraDayData}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpecLike}

import scala.concurrent.duration._

class TimeSeriesDailyActorTest
  extends TestKit(ActorSystem("testSystem"))
    with MustMatchers
    with TestDBProvider
    with WordSpecLike
    with MockitoSugar
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  val mockConfig = mock[Config]
  val testObject = TestActorRef(new TimeSeriesDailyActor(mockConfig, testActor))

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.api.key")).thenReturn("26WTKJT35SAZF6PU")
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
  }

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "TimeSeriesIntraDayActorTest" must {
    "client call test" in {
      val request = DailyStockRequest("TIME_SERIES_INTRADAY", "MSFT")
      testObject ! request
      expectMsg(15 seconds, DbRequest(IntraDayData(null, List()), "intraDay", "save")) //pass after 4pm
//      expectMsg(15 seconds, DbRequest(any[IntraDayData](), "intraDay", "save")) //pass between 9am to 4pm
    }

  }

}
