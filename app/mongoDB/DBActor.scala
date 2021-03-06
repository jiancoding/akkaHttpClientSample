package mongoDB

import akka.actor.{Actor, ActorLogging}
import com.google.inject.Inject
import model.{DbRequest, IntraDayData}
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
  class DBActor @Inject()(db: DB) extends Actor with ActorLogging {

    override def receive: Receive = {
      case DbRequest(data, collectionName, requestType) =>
        log.info(s"got DbRequest: data: $data, collectionName: $collectionName, requestType: $requestType")
        (requestType, data) match {
          case ("save", data: IntraDayData) =>
            save(collectionName, data)
          case (_, _) => log.error("didn't get correct matching DbRequest")

        }
      case _ => sender() ! "malFormatted request!!"
    }

    def save(collectionName: String, data: IntraDayData) = {
      val collection: BSONCollection = db.collection("intraDay")
      val originalSender = sender()
      val futureWriteResult: Future[WriteResult] = collection.insert(data)
      futureWriteResult.map {
        case response if response.ok =>
          originalSender ! s"successfully inserted document $data"
          log.info(s"successfully inserted document")
        case response => log.error("write error: ", response.writeErrors.head)
      }.recover {
        //todo handle MongoError['No primary node is available!
        case e: Exception => originalSender ! s"exception: $e"
      }
    }

    //todo adding and testing more method;upate and delete
    def update(response: IntraDayData) = {
      //    val selector = BSONDocument("symbol" -> response.metaData.symbol)
      //    realDb.update(selector, document)

    }
}


//** example of saving generic type data
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

