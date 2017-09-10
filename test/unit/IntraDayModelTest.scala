package unit

import model.IntraDayData
import model.IntraDayModel._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.io.Source


class IntraDayModelTest extends MustMatchers
  with WordSpecLike
  with MockitoSugar {

  "IntraDayModelTest" must {
    "deserialize to IntraDayResponse" in {
      val jsonString = Source.fromResource("dynamicKey.json").getLines().mkString
      val result = deserializeToIntraDayData(jsonString)

      result mustBe an[IntraDayData]
    }
  }


}
