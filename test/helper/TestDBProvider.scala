package helper

import reactivemongo.api.MongoConnection
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

trait TestDBProvider {
  val database: String = "test-stock-data-management"
  val servers: Seq[String] = List("localhost:27017")
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(servers)

  val db = Await.result(connection.database(database), 10 seconds)

}
