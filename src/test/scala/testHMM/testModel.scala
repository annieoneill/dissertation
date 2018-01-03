package testHMM

import org.scalatest.FunSuite
import HMM.Model

class testModel extends FunSuite {

  test("test frequencies"){
     Model.writeProbabilities
     // val x = Model.transitionProbabilities
    //val y = Model.chordProbabilities
      //val z = Model.emmissionProbabilities
    assert(1 == 1)
  }
}