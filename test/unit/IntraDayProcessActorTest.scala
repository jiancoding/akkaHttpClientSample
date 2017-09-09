package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import client.IntraDayProcessActor
import helper.TestDBProvider
import mongoDB.IntraDayDao
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, MustMatchers, WordSpecLike}

class IntraDayProcessActorTest   extends TestKit(ActorSystem("testSystem"))
  with MustMatchers
  with WordSpecLike
  with TestDBProvider

  with MockitoSugar
  with BeforeAndAfterEach{

  val mockIntraDay = mock[IntraDayDao]
  val testActor1 = TestActorRef(new IntraDayProcessActor(mockIntraDay, db))

  "IntraDayProcessActorTest"must {
    "receive and process request successfully" in {
      val request = IntraDayResponse(null, List())

      testActor1 ! request

    }
  }

}
