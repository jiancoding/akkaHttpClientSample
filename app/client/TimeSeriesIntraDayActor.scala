package client

import javax.inject.Inject

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import client.IntraDayModel.{IntraDayRequest, _}
import com.typesafe.config.Config

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TimeSeriesIntraDayActor @Inject()(config:Config, actor: ActorRef) extends Actor with ActorLogging{

//  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val http = Http(context.system)

  var infoMessage = ""

  override def receive: Receive = {
    case request: IntraDayRequest => {
      val paraMap = Map (
        "function" -> request.function,
        "symbol" -> request.symbol,
        "interval" -> request.interval,
        "apikey" -> request.apiKey
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
      val intraDayResponse: IntraDayResponse = deserializeToObj(jsonString)
      actor ! intraDayResponse
//      actor ! "success"
      infoMessage = "success"
    }
  }


  def createUrl(baseUrl: String, paraMap: Map[String, String]): String = {
    val subString = paraMap.filter(x => !x._2.isEmpty).map {
      case (key, value) => s"$key=$value"
    }.mkString("&")
    s"$baseUrl$subString"
  }
}


