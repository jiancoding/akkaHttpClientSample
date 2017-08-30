package unit

import client.IntraDayModel._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.io.Source


class IntraDayModelTest extends MustMatchers
  with WordSpecLike
  with MockitoSugar {

  "IntraDayModelTest" must {
    "deserialize to IntraDayResponse" in {
      val jsonString = Source.fromResource("dynamicKey.json").getLines().mkString
      val result = deserializeToObj(jsonString)

      result mustBe an[IntraDayResponse]
    }
  }


}
