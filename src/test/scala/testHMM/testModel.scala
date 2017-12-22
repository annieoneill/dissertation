package generativeHarmoniser.src.test.scala.testHMM

import org.scalatest.FunSuite
import HMM.Model

class testModel extends FunSuite {

  test("test frequencies"){
    val x = Model.transitionProbabilities
    val y = Model.chordProbabilities
    val z = Model.emmissionProbabilities
    assert(y.sum == 1)
  }
}
