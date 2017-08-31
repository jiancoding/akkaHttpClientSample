package unit

import client.IntraDayModel.{IntraDayResponse, MetaData, TimeSeriesData}
import mongoDB.{IntraDayDao, MongoDao}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}

class IntraDayDaoTest extends MustMatchers
  with WordSpecLike
  with MockitoSugar
  with MongoDao{
  "IntraDayDaoTest" must {
    val testObject = new IntraDayDao

    "saveing correct record" in {
      val response = IntraDayResponse (
        MetaData("info", "symbol", "lastRefreshed", "1min", "outpuSize", "easternTime"),
        List(TimeSeriesData("timeslot2", "open", "high", "low", "close", "volume"))
      )
      testObject.save(response)

    }
  }

}
