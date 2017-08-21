package swagger


import akka.http.scaladsl.server.Directives

trait SwaggerSite extends Directives {
  val swaggerSiteRoute = path("swagger") { getFromResource("swagger/index.html") } ~ getFromResourceDirectory("swagger")
}

