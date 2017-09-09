package unit

import helper.TestDBProvider
import model.IntraDay
import mongoDB.IntraDayDao
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import reactivemongo.api.collections.bson.BSONCollection

class IntraDayDaoTest extends MustMatchers
  with WordSpecLike
  with MockitoSugar
  with TestDBProvider{

  val testObject = new IntraDayDao
  val collection: BSONCollection = db.collection("intraDay")


  "IntraDayDaoTest" must {

    "saveing correct record" in {
      val intraDayData = IntraDay( "symbol1-IntraDayDaoTest", List())
      testObject.save(collection, intraDayData)
      //has to wait since it's asynchronous saving
      Thread.sleep(5000)
    }



  }

}
