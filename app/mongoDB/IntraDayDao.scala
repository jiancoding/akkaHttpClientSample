package mongoDB

import client.IntraDayModel.IntraDayRe
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONArray, BSONDocument}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class IntraDayDao@Inject()(db: DB) extends LazyLogging {
  val collection: BSONCollection = db.collection("intraDay")

  def save(response: IntraDayResponse) = {
    val document1 = BSONDocument(
      "symbol" -> response.metaData.symbol,
      "TimeSeries" -> BSONArray {
        BSONDocument(
          "timestamp" -> response.timeSeries.head.timeslot,
          "open" -> response.timeSeries.head.open,
          "high" -> response.timeSeries.head.high,
          "low" -> response.timeSeries.head.low,
          "close" -> response.timeSeries.head.close,
          "volume" -> response.timeSeries.head.volume
        )
      }
    )

    val writeResult: Future[WriteResult] = collection.insert(document1)

    writeResult.onComplete {
      case Failure(e) =>
        logger.error("failed to write intraDay response record", e.printStackTrace())
      case Success(result) =>
        logger.info(s"successfully inserted document with result: $result")
        println(s"successfully inserted document with result: $result")
    }
  }

  //todo adding and testing more method;upate and delete
  def update(response: IntraDayResponse) = {
    //    val selector = BSONDocument("symbol" -> response.metaData.symbol)
    //    realDb.update(selector, document)

  }


}
