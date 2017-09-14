package main

import add.{AddActor, AddService}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import client.{TimeSeriesDailyActor, TimeSeriesDailyService}
import com.google.inject.Guice
import com.typesafe.config.Config
import hello.{HelloActor, HelloService}
import modules.GuiceBindingModule
import mongoDB.DBActor
import reactivemongo.api.DB
import swagger.{SwaggerDocService, SwaggerSite}

object Rest extends App with RouteConcatenation with SwaggerSite {
  implicit val system = ActorSystem("stock-data-sample")
  sys.addShutdownHook(system.terminate())
  //  https://stackoverflow.com/questions/26541784/how-to-select-akka-actor-with-actorselection

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private val injector = Guice.createInjector(new GuiceBindingModule)
  val db = injector.getInstance(classOf[DB])
  val config = injector.getInstance(classOf[Config])

  val add = system.actorOf(Props[AddActor])
  val hello = system.actorOf(Props[HelloActor])
  val dbActor = system.actorOf(Props(new DBActor(db)))
  val timeSeriesIntraDayActor = system.actorOf(Props(new TimeSeriesDailyActor(config, dbActor)))

  val routes =
    cors()(
      new AddService(add).route ~
        new HelloService(hello).route ~
        new TimeSeriesDailyService(timeSeriesIntraDayActor).route ~
        swaggerSiteRoute ~
        SwaggerDocService.routes)

  Http().bindAndHandle(routes, "0.0.0.0", 12345)
}