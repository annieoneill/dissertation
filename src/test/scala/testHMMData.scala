package generativeHarmoniser.src.test.scala

import org.scalatest.FunSuite
import generativeHarmoniser.src.main.scala.HMMData._
import generativeHarmoniser.src.main.scala.midiFileReader

class testHMMData extends FunSuite {

  test("test finding notes on") {
    def data:Array[(Boolean,Int,Double)] = Array((true,1,10),(true,2,10),(false,2, 20),(true,3,20),(true,3,22))

    def map  = getVector(21,data)

    assert(map(2) == 0)
    assert(map(1) == 1)
    assert(map(12) == 0)
    assert(map(3)== 1)
  }

  test("test labelling") {
    val x = trainingValues("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid", "src/test/resources/01_-_I_Saw_Her_Standing_There.lab")
    assert(x.size == midiFileReader.getSeq("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid").getTickLength./(1000) -1 )
  }
  test("normalise chord data"){

  }

}