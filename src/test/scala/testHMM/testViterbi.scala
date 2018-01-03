package testHMM

import org.scalatest.FunSuite
import HMM.Viterbi._

abstract class testViterbi extends FunSuite {

  test("testing get current best") {
    val prev = Array((0.3,0),(0.4,1),(0.5,1))
    val trans = Array(Array(0.2,0.3,0.3),Array(0.5,0.4,0.5),Array(0.4,0.9,0.3))
    val emm = Array(Array(0.6,0.1,0.2,0.4),Array(0.6,0.1,0.2,0.4),Array(0.6,0.1,0.2,0.4))
    val obv = 3
    val x = currentBest(prev,trans,emm,obv)
    val arr = Array((0.08F,1),(0.18,2),(0.08,1))
    assert(x.deep == arr.deep)
  }

  test("testing backtrack") {
    val arr: Array[Array[(Double, Int)]] = Array(Array((2.0,0),(3.0,0),(2.0,0)),Array((2.0,0),(3,1),(4,2)),Array((2.0,2),(3,1),(4,0)))
    val x = backtrack(arr,3)
    assert(x.deep == Array(0,0,2).deep)
  }

  test("testing viterbi") {

  }

}
