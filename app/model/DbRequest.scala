package model

case class DbRequest(data: IntraDayData, collectionName: String, requestType: String)
//case class DbRequest2[T <: StockData](data: T)

trait StockData {
  def save
}



