//package com.example.akka.swagger
//
//import akka.http.scaladsl.server.Directives
//
//trait SwaggerSite extends Directives {
//  val swaggerSiteRoute =
//    path("swagger") { getFromResource("swagger/swagger.scala.html") } ~
//      getFromResourceDirectory("swagger")
//}
//
//object MyApp extends App with SwaggerSite