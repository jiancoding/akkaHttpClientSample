package unit

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import client.TimeSeriesIntraDayService
import org.scalatest.{FlatSpec, Matchers}

class TimeSeriesIntraDayServiceTest extends FlatSpec with Matchers with ScalatestRouteTest {
//  implicit val system = ActorSystem("akka-http-sample")

  val testRoutes = new TimeSeriesIntraDayService().route

  "Service" should "respond to single IP query" in {
    Get("/?function=funcA&symbol=symbol&interval=1min&apiKey=apiKey") ~> testRoutes ~> check {
      responseAs[String] shouldEqual "The color is 'blue' and the background is 'red'"
    }

  }




}
