package unit

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestActorRef
import client.{TimeSeriesDailyActor, TimeSeriesDailyService}
import com.typesafe.config.Config
import helper.TestDBProvider
import mongoDB.DBActor
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}
import spray.json._


class TimeSeriesDailyServiceTest
//  extends TestKit(ActorSystem("testSystem"))
  extends Matchers
    with WordSpecLike
    with MockitoSugar
    with TestDBProvider
    with ScalatestRouteTest
    with BeforeAndAfterEach
    with BeforeAndAfterAll {

  val mockConfig = mock[Config]
  val dbActor = TestActorRef(new DBActor(db))
  val intraDayActor = TestActorRef(new TimeSeriesDailyActor(mockConfig, dbActor))
  val testRoutes = new TimeSeriesDailyService(intraDayActor).route

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
    when(mockConfig.getString("alpha.vantage.api.key")).thenReturn("26WTKJT35SAZF6PU")
  }

  "TimeSeriesIntraDayServiceTes" must {

    "respond to single IP query" in {
      val jsonData =
        """
          |{
          | "function": "TIME_SERIES_INTRADAY",
          | "symbol": "MSFT",
          | "interval": "1min"
          |
          |}
        """.stripMargin.parseJson

      Post("/stock", jsonData) ~> testRoutes ~> check {
        responseAs[String] shouldEqual "success!"
      }
    }

  }
}
