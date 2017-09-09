package model

import reactivemongo.bson.Macros

case class IntraDay(symbol: String, timeSeries: List[TimeSeries])
case class TimeSeries(timestamp: String, open: String, high: String, low: String, close: String, volume: String)

object IntraDay {

  implicit val timeSeriesHandler = Macros.handler[TimeSeries]
  implicit val intraDayHandler = Macros.handler[IntraDay]
}
