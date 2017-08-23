package client

import akka.actor.{Actor, ActorLogging}
import spray.json.DefaultJsonProtocol


object IntraDayModel extends DefaultJsonProtocol{
  case class IntraDayRequest(function: String,
                             symbol:String,
                             interval:String,
                             outputSize: Option[String],
                             dataType: Option[String],
                             apiKey: String)


//  case class IntraDayResponse(`Meta Data`: MetaData, timeSeries: List[TimeSeriesData])
  case class IntraDayResponse(`Meta Data`: MetaData)


  case class MetaData(`1. Information`: String,
                      `2. Symbol`: String,
                      `3. Last Refreshed`: String,
                      `4. Interval`: String,
                      `5. Output Size`: String,
                      `6. Time Zone`: String)

  implicit val metaDataFormat = jsonFormat6(MetaData.apply)

  case class TimeSeriesData(open: String, high: String, low: String, close: String, volume: String)

  implicit val timeSeriesDataFormat = jsonFormat5(TimeSeriesData.apply)

  implicit val intraDayResponseFormat = jsonFormat1(IntraDayResponse.apply)


}

class IntraDay extends Actor with ActorLogging {
  override def receive: Receive = ???
}


