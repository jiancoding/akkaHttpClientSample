package mongoDB

import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


trait MongoDao {
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost"))

  def dbFromConnection(connection: MongoConnection): Future[BSONCollection] =
    connection.database("stock-database").
      map(_.collection("intraDayCollection"))

  val dbFuture = dbFromConnection(connection)
  val realDb: BSONCollection = Await.result(dbFuture, 10 seconds)

}
