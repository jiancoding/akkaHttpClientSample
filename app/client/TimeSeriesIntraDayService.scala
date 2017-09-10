package client

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import model.IntraDayModel.IntraDayRequest
import hello.HelloActor.{Greeting, Hello}

import scala.concurrent.Future
import scala.concurrent.duration._

class TimeSeriesIntraDayService extends Directives{

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val baseUrl = "https://www.alphavantage.co/query?"
  implicit val timeout = Timeout(2.seconds)
  val route = getData()
  lazy val apiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(baseUrl)

  def apiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(apiConnectionFlow).runWith(Sink.head)

  def getData() =
    parameters('function, 'symbol, 'interval, 'apiKey) { (function, symbol,interval, apiKey ) =>

     complete(s"The color is '$function' and the background is '$symbol'")
  }





}
