package client

import java.io.IOException
import javax.inject.Inject

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import helper.Utilities
import model.StockRequest.DailyStockRequest

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


class RestClientActor @Inject()(config: Config)
  extends Actor
    with Utilities
    with ActorLogging {

  private lazy val apiKey = config.getString("alpha.vantage.api.key")
  private lazy val baseUrl = config.getString("alpha.vantage.base.url")
  private implicit val materializer = ActorMaterializer()
  private val http = Http(context.system)

  override def receive: Receive = {
    case DailyStockRequest(function, symbol) => {
      log.info("RestClientActor got request ............")
      val paraMap = Map(
        "function" -> function,
        "symbol" -> symbol,
        "apikey" -> apiKey
      )
      val url = createUrl(baseUrl, paraMap)
      val req = HttpRequest(HttpMethods.GET, url)
      val senderRef = sender()

      val responseString = http.singleRequest(req).flatMap { response =>
        response.status match {
          case StatusCodes.OK =>
            Unmarshal(response.entity).to[String]
          case StatusCodes.BadRequest =>
            log.error(s"Bad request: response status code : ${response.status}")
            Future.successful {
              s"Bad request: response status code : ${response.status}"
            }
          case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
            val errorMessage = s"error message from AlphaVantage: $entity"
            log.error(errorMessage)
            Future.failed(new IOException(errorMessage)) //todo how to handle exception in actor??
          }
        }
      }
      val resultString = Await.result(responseString, 5 seconds).toString
      log.info(s"response String $resultString")

      senderRef ! resultString
    }
  }
}