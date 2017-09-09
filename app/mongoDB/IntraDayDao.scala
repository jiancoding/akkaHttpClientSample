package mongoDB

import client.IntraDayModel.IntraDayRe.IntraDayResponse
import com.typesafe.scalalogging.LazyLogging
import model.IntraDay
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class IntraDayDao extends LazyLogging {

  def save(collection: BSONCollection, intraDay: IntraDay) = {
    val writeResult: Future[WriteResult] = collection.insert(intraDay)
    writeResult.onComplete {
      case Failure(e) =>
        logger.error("failed to write intraDay response record", e.printStackTrace())
      case Success(result) =>
        logger.info(s"successfully inserted document with result: $result")
    }
  }

  //todo adding and testing more method;upate and delete
  def update(response: IntraDayResponse) = {
    //    val selector = BSONDocument("symbol" -> response.metaData.symbol)
    //    realDb.update(selector, document)

  }


}
