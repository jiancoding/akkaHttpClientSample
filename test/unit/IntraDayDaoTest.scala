package unit

import client.IntraDayModel.{IntraDayResponse, MetaData, TimeSeriesData}
import helper.TestDBProvider
import mongoDB.IntraDayDao
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}

class IntraDayDaoTest extends MustMatchers
  with WordSpecLike
  with MockitoSugar
  with TestDBProvider{

  "IntraDayDaoTest" must {
    val testObject = new IntraDayDao(db)

    "saveing correct record" in {
      val response = IntraDayResponse (
        MetaData("info", "symbol", "lastRefreshed", "1min", "outpuSize", "easternTime"),
        List(TimeSeriesData("timeslot3", "open", "high", "low", "close", "volume"))
      )
      testObject.save(response)
    }

  }

}
