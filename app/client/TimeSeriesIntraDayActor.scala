package client

import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import client.IntraDayModel.{IntraDayRequest, _}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.Source


class TimeSeriesIntraDayActor extends Actor with ActorLogging{

  implicit val system = ActorSystem()
//  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val baseUrl = "https://www.alphavantage.co/query?"
  val http = Http(context.system)

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
            Unmarshal(response.entity).to[String]
        }
      }
      val z = Await.result(result, 10.seconds)
      val x = Source.fromResource("dynamicKey.json").getLines().mkString
      deserializeToObj(x)
    }
  }


  def createUrl(baseUrl: String, paraMap: Map[String, String]): String = {
    val subString = paraMap.filter(x => !x._2.isEmpty).map {
      case (key, value) => s"$key=$value"
    }.mkString("&")
    s"$baseUrl$subString"
  }
}
