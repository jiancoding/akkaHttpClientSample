package unit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import helper.TestDBProvider
import model.{DbRequest, IntraDayData}
import mongoDB.DBActor
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.Await
import scala.concurrent.duration._

class DBActorTest extends TestKit(ActorSystem("testSystem"))
  with MustMatchers
  with WordSpecLike
  with MockitoSugar
  with TestDBProvider {

  implicit val timeout = Timeout(5.seconds)
  val testObject = TestActorRef(new DBActor(db))
  val collection: BSONCollection = db.collection("intraDay")

  "IntraDayDaoTest" must {

    "saveing correct record" in {
      val intraDayData = IntraDayData("symbol1-IntraDayDaoTest", List())
      val request = DbRequest(intraDayData, "DBActorTest", "save")

      val futureResponse = testObject ? request
      val responseMessage = Await.result(futureResponse, 5 seconds)

      responseMessage.asInstanceOf[String] must include ("successfully inserted")
    }

    "handling bad format record" in {
      val malFormattedRequest = "abc"

      val futureResponse = testObject ? malFormattedRequest
      val responseMessage = Await.result(futureResponse, 5 seconds)

      responseMessage.asInstanceOf[String] must include ("malFormatted request")
    }

  }

}
