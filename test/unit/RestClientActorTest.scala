package unit

import akka.actor.ActorSystem
import akka.pattern._
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import client.RestClientActor
import com.typesafe.config.Config
import helper.TestDBProvider
import model.StockRequest.DailyStockRequest
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._

class RestClientActorTest extends TestKit(ActorSystem("testSystem"))
  with MustMatchers
  with TestDBProvider
  with WordSpecLike
  with MockitoSugar
  with BeforeAndAfterEach
  with BeforeAndAfterAll {

  implicit val timeout = Timeout(5.seconds)
  val mockConfig = mock[Config]
  val testObject = TestActorRef(new RestClientActor(mockConfig))

  override def beforeEach(): Unit = {
    super.beforeEach()
    when(mockConfig.getString("alpha.vantage.api.key")).thenReturn("26WTKJT35SAZF6PU")
    when(mockConfig.getString("alpha.vantage.base.url")).thenReturn("https://www.alphavantage.co/query?")
  }

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "RestClientActorTest" must {

    "handle intraday stockRequest with correct function parameters successfully" in {
      val request = DailyStockRequest("TIME_SERIES_INTRADAY", "MSFT")
      val futureResponse = testObject ? request

      val response = Await.result(futureResponse, 5 seconds)
      response.asInstanceOf[String] must include("Error Message")
    }

    "handle daily stockRequest with correct parameters successfully" in {
      val request = DailyStockRequest("TIME_SERIES_DAILY", "MSFT")
      val futureResponse = testObject ? request

      val response = Await.result(futureResponse, 5 seconds)
      response.asInstanceOf[String] must include("Meta Data")
    }



  }


}
