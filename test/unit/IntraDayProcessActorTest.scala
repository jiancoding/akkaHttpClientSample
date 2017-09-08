package unit

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import client.IntraDayModel.IntraDayRe
import client.IntraDayModel.IntraDayRe.IntraDayResponse
import client.{IntraDayProcessActor, TimeSeriesIntraDayActor}
import org.scalatest.{BeforeAndAfterEach, MustMatchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar

class IntraDayProcessActorTest   extends TestKit(ActorSystem("testSystem"))
  with MustMatchers
  with WordSpecLike
  with MockitoSugar
  with BeforeAndAfterEach{

  val testActor1 = TestActorRef(new IntraDayProcessActor)

  "IntraDayProcessActorTest"must {
    "receive and process request successfully" in {
      val request = IntraDayResponse(null, List())

      testActor1 ! request

    }
  }

}
