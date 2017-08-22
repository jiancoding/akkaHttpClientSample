package unit

import akka.http.scaladsl.testkit.ScalatestRouteTest
import client.TimeSeriesDataService
import org.scalatest.{Matchers, WordSpec}

class TimeSeriesDataTest extends WordSpec with Matchers with ScalatestRouteTest{
  "TimeSeriesDataTest" must {
    "client call test" in {
      val testObject = new TimeSeriesDataService


    }
  }

}
