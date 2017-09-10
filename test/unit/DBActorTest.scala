package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import helper.TestDBProvider
import model.{DbRequest, IntraDayData}
import mongoDB.DBActor
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import reactivemongo.api.collections.bson.BSONCollection

class DBActorTest extends TestKit(ActorSystem("testSystem"))
  with MustMatchers
  with WordSpecLike
  with MockitoSugar
  with TestDBProvider {

  val testObject = TestActorRef(new DBActor(db))
  val collection: BSONCollection = db.collection("intraDay")

  "IntraDayDaoTest" must {

    "saveing correct record" in {
      val intraDayData = IntraDayData("symbol1-IntraDayDaoTest", List())
      val request = DbRequest(intraDayData, "DBActorTest", "save")
      testObject ! request
      //has to wait since it's asynchronous saving
      Thread.sleep(5000)
    }


  }

}
