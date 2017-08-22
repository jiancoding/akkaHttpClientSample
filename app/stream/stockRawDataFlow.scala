package stream

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream._
import akka.stream.scaladsl.{Flow, Keep, Sink}
import com.typesafe.scalalogging.StrictLogging
import org.apache.avro.generic.GenericRecord
import spray.json.JsValue

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class stockRawDataFlow(actorSystem: ActorSystem) extends StrictLogging{
  private val decider: Supervision.Decider = {
    case e: Exception => logger.error("Exception was thrown in stockRawDataFlow.", e)
      Supervision.Resume
  }

  private val topic = "topic1"
  private implicit val system = actorSystem
  private val settings = ActorMaterializerSettings(system).withSupervisionStrategy(decider).withDebugLogging(true)
  private implicit val materializer = ActorMaterializer(settings)

  private val consumerSettings = ConsumerSettings[String, GenericRecord](actorSystem, None, None)
  private val consumer = Consumer.plainSource(consumerSettings, Subscriptions.topics(topic))

  //todo needs to store kafka offset in db
  private val source = consumer.map { record =>
    val genericRecord = record
    //parse record and map to model object
  }

  private val processRecord = ??? //process record if necessary

  private val apiServiceCall = Flow[String].map { st =>
    //make api Service call to get data
  }

  private val publishRecord = ???

  private val saveRecord = Flow[JsValue].map { jsValue =>
    //saving record to mongoDB
  }

  private var killSwitch : UniqueKillSwitch = _
  private var future : Future[Done] = _


  private val graph = source
    .viaMat(KillSwitches.single)(Keep.right)
   //   .viaMat(apiServiceCall)
//    .viaMat(saveRecord)()
    //.viaMat(publishRecord())publish specific topic
    .toMat(Sink.ignore)(Keep.both)

  def start() = {
    val tuple = graph.run()
    killSwitch = tuple._1
    future = tuple._2
    future
  }

  def stop() = {
    killSwitch.shutdown()
    Await.result(future, 5 seconds)

  }


}
