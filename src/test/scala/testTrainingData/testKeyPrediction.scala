package testTrainingData

import org.scalatest.FunSuite
import trainingData.keyPrediction._


class testKeyPrediction extends FunSuite {

  test("test whole piece vector") {
    val x: Array[(Boolean, Int, Double)] = Array((true,1,3.0),(false,1,5.0),(true,2,4.0),(false,2,7.0),(true,1,6.0),(false,1,8.0))
    val map = wholePieceVector(x)
    assert(map.get(1).contains(4.0))
    assert(map.get(2).contains(3.0))
    assert(map.get(0).contains(0.0))
  }
}
