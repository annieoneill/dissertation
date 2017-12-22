package generativeHarmoniser.src.test.scala

import org.scalatest.FunSuite
import negative.negativeHarmony._

class testNegativeHarmoniser extends FunSuite {

  test("testing invert notes"){
    val x: Array[Int] = invertNotes(Array(1,2,3,4), 5)
    assert(x.length == 4)
    assert(x.deep == Array(4,3,2,1).deep)
  }

}