package swagger

import add.AddService
import client.TimeSeriesIntraDayService
import com.github.swagger.akka.model.Info
import com.github.swagger.akka.SwaggerHttpService
import hello.HelloService
import io.swagger.models.auth.BasicAuthDefinition

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses = Set(classOf[AddService], classOf[HelloService], classOf[TimeSeriesIntraDayService])
  override val host = "localhost:12345"
  override val info = Info(version = "1.0", title = "stock-data-api", description = "description")
  override val apiDocsPath = "api-docs"
  override val basePath = "/"
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions = Seq("Function1", "Function1RequestContextFutureRouteResult")
}
