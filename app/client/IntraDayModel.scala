package client

import akka.actor.{Actor, ActorLogging}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.github.nscala_time.time.Imports._
import com.typesafe.scalalogging.LazyLogging
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

object IntraDayModel extends LazyLogging{

  case class IntraDayRequest(function: String,
                             symbol: String,
                             interval: String,
                             outputSize: Option[String],
                             dataType: Option[String],
                             apiKey: String)

  case class IntraDayResponse(`Meta Data`: MetaData, timeSeries: Seq[TimeSeriesData])

  case class MetaData(`1. Information`: String,
                      `2. Symbol`: String,
                      `3. Last Refreshed`: String,
                      `4. Interval`: String,
                      `5. Output Size`: String,
                      `6. Time Zone`: String)


  case class TimeSeriesData(open: String, high: String, low: String, close: String, volume: String)

  def deserializeToObj(jsvalue: String): IntraDayResponse = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)

    //todo might be better way https://stackoverflow.com/questions/17685508/jackson-deserialization-with-unknown-dynamic-properties
    val parsedMap = mapper.readValue(jsvalue, classOf[Map[String, Map[String, Any]]])

    try {
      //populate IntraDayResponse object
      val metaData = MetaData(
        parsedMap("Meta Data")("1. Information").toString,
        parsedMap("Meta Data")("2. Symbol").toString,
        parsedMap("Meta Data")("3. Last Refreshed").toString,
        parsedMap("Meta Data")("4. Interval").toString,
        parsedMap("Meta Data")("5. Output Size").toString,
        parsedMap("Meta Data")("6. Time Zone").toString
      )
      val pastMinTimeLine = getPastMin(DateTime.now, 5).map(formatTime)

      val data = pastMinTimeLine.map { timeSlot =>
        val timeSeriesDataMap = parsedMap("Time Series (1min)")(timeSlot).asInstanceOf[HashMap[String, String]]
        TimeSeriesData(
          timeSeriesDataMap("1. open"),
          timeSeriesDataMap("2. high"),
          timeSeriesDataMap("3. low"),
          timeSeriesDataMap("4. close"),
          timeSeriesDataMap("5. volume")
        )
      }
      IntraDayResponse(metaData, data)
    } catch {
      case e: NoSuchElementException => logger.error("exception in deserialize response " + e)
        null
    }
  }

  def getPastMin(dateTime: DateTime, n: Int): Seq[DateTime] = {
    var list = new ListBuffer[DateTime]()
    list +=dateTime.second(0)
    for (a <- 1 until n) {
      list += (dateTime - a.minutes.toPeriod).second(0)
    }
    list.toList
  }

  def formatTime(dateTime: DateTime) = {
    val dtf: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    dateTime.toString(dtf)
  }

}

class IntraDay extends Actor with ActorLogging {
  override def receive: Receive = ???
}


