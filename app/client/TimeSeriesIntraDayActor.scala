package client

import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import client.IntraDayModel.{IntraDayRequest, IntraDayResponse}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TimeSeriesIntraDayActor extends Actor with ActorLogging{

  implicit val system = ActorSystem()
//  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

//  private implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

//  val apiKey = "26WTKJT35SAZF6PU"
//  val paraMap = Map (
//    "function" -> "TIME_SERIES_INTRADAY",
//    "symbol" -> "MSFT",
//    "interval" -> "1min",
//    "apiKey" -> apiKey
//  )

  val baseUrl = "https://www.alphavantage.co/query?"
  val http = Http(context.system)

//  override def preStart() = {
//    val req = HttpRequest(HttpMethods.GET, uri = createUrl(baseUrl, paraMap))
//    http.singleRequest(req).pipeTo(self)
//  }
  //todo has to deal with dynamic key in json mapping

  override def receive: Receive = {
    case request: IntraDayRequest => {
      val pMap = Map (
        "function" -> request.function,
        "symbol" -> request.symbol,
        "interval" -> request.interval,
        "apikey" -> request.apiKey
      )
      val url = createUrl(baseUrl, pMap)
      val req = HttpRequest(HttpMethods.GET, url)
      val resFuture = http.singleRequest(req)

      val result = resFuture.flatMap { response =>
        response.status match {
          case StatusCodes.OK =>
            Unmarshal(response.entity).to[IntraDayResponse]
        }
      }

      val z = Await.result(result, 5.seconds)
      log.info("z  ===== " + z)
      z
    }
  }


  def createUrl(baseUrl: String, paraMap: Map[String, String]): String = {
    val subString = paraMap.filter(x => !x._2.isEmpty).map {
      case (key, value) => s"$key=$value"
    }.mkString("&")
    s"$baseUrl$subString"
  }
}
