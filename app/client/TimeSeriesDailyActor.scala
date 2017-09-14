package client

import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import model.IntraDayModel._
import model.StockRequest.DailyStockRequest
import model.{DbRequest, IntraDayData}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class TimeSeriesDailyActor @Inject()(config: Config, dbActor: ActorRef)
  extends Actor
    with ActorLogging {

  implicit val materializer = ActorMaterializer()
  val http = Http(context.system)
  val collectionName = "intraDay"
  private lazy val apiKey = config.getString("alpha.vantage.api.key")

  override def receive: Receive = {
    case DailyStockRequest(function, symbol) => {
      log.info("TimeSeriesIntraDayActor request: ............")
      val paraMap = Map(
        "function" -> function,
        "symbol" -> symbol,
        "apikey" -> apiKey
      )
      val baseUrl = config.getString("alpha.vantage.base.url")
      val url = createUrl(baseUrl, paraMap)
      val req = HttpRequest(HttpMethods.GET, url)
      val resFuture = http.singleRequest(req)

      val result = resFuture.flatMap { response =>
        response.status match {
          case StatusCodes.OK =>
            Unmarshal(response.entity).to[String]
        }
      }
      val jsonString = Await.result(result, 10.seconds)
      val intraDayData: IntraDayData = deserializeToIntraDayData(jsonString)
      //has to convert to DBRequest
      dbActor ! DbRequest(intraDayData, collectionName, "save")
    }
    case _ => log.error("The request type is not StockRequest!!!")

  }

  def createUrl(baseUrl: String, paraMap: Map[String, String]): String = {
    val subString = paraMap.filter(_._2.nonEmpty).map {
      case (key, value) => s"$key=$value"
    }.mkString("&")
    s"$baseUrl$subString"
  }
}

//**** 2nd way to send http request
// lazy val apiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
//Http().outgoingConnection(baseUrl)
//
//def apiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(apiConnectionFlow).runWith(Sink.head)
//