package client

import akka.actor.Actor
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import model.{IntraDay, TimeSeries}
import mongoDB.IntraDayDao
import reactivemongo.api.DB
import reactivemongo.api.collections.bson.BSONCollection

class IntraDayProcessActor@Inject()(intraDayDao: IntraDayDao, db: DB) extends Actor with LazyLogging {
  override def receive: Receive = {
    case request: IntraDayResponse => {
      logger.info("request: ", request.toString)
      val collection: BSONCollection = db.collection("intraDay")
      val intraDayObject = IntraDay(
        request.metaData.symbol,
        List(
          TimeSeries(request.timeSeries.head.timeslot,
            request.timeSeries.head.open,
            request.timeSeries.head.high,
            request.timeSeries.head.low,
            request.timeSeries.head.close,
            request.timeSeries.head.volume
          )
        )
      )
      intraDayDao.save(collection, intraDayObject)
    }

    case _ =>
      logger.info("get Something ......")
  }
}

