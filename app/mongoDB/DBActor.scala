package mongoDB

import akka.actor.Actor
import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import model.{DbRequest, IntraDayData}
import model.IntraDayModel.IntraDayResponse
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class DBActor @Inject()(db: DB) extends Actor with LazyLogging {

  def receive: Receive =  {
    case DbRequest(data, collectionName, requestType) =>
      (requestType, data) match {
        case ("save", data: IntraDayData) => save(collectionName, data)

      }
  }

  def save(collectionName: String, data: IntraDayData) = {
    val collection: BSONCollection = db.collection("intraDay")
    val writeResult: Future[WriteResult] = collection.insert(data)
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


//case class EventOperation[T <: Event](eventType: T)
//
//class OperationActor extends Actor {
//
//  def receive = {
//    case EventOperation(eventType) => eventType.execute
//  }
//
//}
//
//trait Event {
//  def execute //implement execute in specific event class
//}
//
//class Event1 extends Event {/*execute implemented with business logic*/}
//class Event2 extends Event {/*execute implemented with business logic*/}

