package com.example.akka.swagger

import com.example.akka.add.AddService
import com.example.akka.hello.HelloService
import com.github.swagger.akka.model.Info
import com.github.swagger.akka.{SwaggerHttpService, SwaggerSite}
import io.swagger.models.auth.BasicAuthDefinition

object SwaggerDocService extends SwaggerHttpService with SwaggerSite {
  override val apiClasses = Set(classOf[AddService], classOf[HelloService])
  override val host = "localhost:12345"
  override val info = Info(version = "1.0", title = "Swagger-akka-http-sample-api", description = "good description")
  override val apiDocsPath = "api-docs"
  override val basePath = "/"
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}
