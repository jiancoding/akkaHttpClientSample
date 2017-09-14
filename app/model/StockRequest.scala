package model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object StockRequest extends DefaultJsonProtocol with SprayJsonSupport {
  case class DailyStockRequest(function: String, symbol: String)
  implicit val dailyStockRequestFormat = jsonFormat2(DailyStockRequest)
}
