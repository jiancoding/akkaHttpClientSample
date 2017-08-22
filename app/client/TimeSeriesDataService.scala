package client

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}

class TimeSeriesDataService extends Actor with Directives with ActorLogging{

  import akka.pattern.pipe
  import context.dispatcher

  private implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val apiKey = "26WTKJT35SAZF6PU"
  val paraMap = Map (
    "function" -> "TIME_SERIES_INTRADAY",
    "symbol" -> "MSFT",
    "interval" -> "1min",
    "apiKey" -> apiKey
  )

  val baseUrl = "https://www.alphavantage.co/query?"
  val http = Http(context.system)

  override def preStart() = {
    val req = HttpRequest(HttpMethods.GET, uri = createUrl(baseUrl, paraMap))
    http.singleRequest(req).pipeTo(self)
  }


  override def receive: Receive = {
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      log.info("entity = " + entity)
  }


  def createUrl(baseUrl: String, paraMap: Map[String, String]): String = {
    paraMap.filter(_._2.isEmpty).map {
      case (key, value) => s"$key=$value"
    }.mkString(baseUrl, "&", "")
  }
}
