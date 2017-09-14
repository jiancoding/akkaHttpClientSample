package client

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import io.swagger.annotations._
import model.StockRequest._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

//todo create service to handle post csv files or post request of symbol

@Api(value = "/stock", produces = "application/json")
@Path("/stock")
class TimeSeriesDailyService(actor: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with LazyLogging {
  implicit val timeout = Timeout(2.seconds)

  val route = getStockSymbols
  @ApiOperation(value = "post stock request", nickname = "postStockRequest", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", value = "stock-symbols interested", required = true,
      dataTypeClass = classOf[DailyStockRequest], paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Posting stock request successfully"),
    new ApiResponse(code = 400, message = "Bad request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getStockSymbols = path("stock") {
    post {
      entity(as[DailyStockRequest]) { request =>
        logger.info(s"TimeSeriesIntraDayService request: ............" ,request.toString)
        actor ! request
        complete("success!")
      }
    }
  }
}
