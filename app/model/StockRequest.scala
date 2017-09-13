package model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

object StockRequest extends DefaultJsonProtocol with SprayJsonSupport {
  case class StockRequest(function: String, symbol: String, interval: String)
  implicit val stockRequestFormat = jsonFormat3(StockRequest)
}
