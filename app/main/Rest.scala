package main

import add.{AddActor, AddService}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import hello.{HelloActor, HelloService}
import swagger.{SwaggerDocService, SwaggerSite}

object Rest extends App with RouteConcatenation with SwaggerSite {
  implicit val system = ActorSystem("akka-http-sample")
  sys.addShutdownHook(system.terminate())

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val add = system.actorOf(Props[AddActor])
  val hello = system.actorOf(Props[HelloActor])
  val routes =
    cors() (new AddService(add).route ~
      new HelloService(hello).route ~
      swaggerSiteRoute ~
      SwaggerDocService.routes)

  Http().bindAndHandle(routes, "0.0.0.0", 12345)
}