package model

import reactivemongo.bson.Macros

case class IntraDayData(symbol: String, timeSeries: Seq[TimeSeries])

case class TimeSeries(timestamp: String, open: String, high: String, low: String, close: String, volume: String)

object IntraDayData {

  implicit val timeSeriesHandler = Macros.handler[TimeSeries]
  implicit val intraDayHandler = Macros.handler[IntraDayData]
}
