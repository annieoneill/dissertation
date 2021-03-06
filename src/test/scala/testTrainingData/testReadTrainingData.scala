package testTrainingData

import org.scalatest.FunSuite
import trainingData.chordData._
import trainingData.keyData._

class testReadTrainingData extends FunSuite {

  test("testing reading chord data"){
    val chord = chords("src/test/resources/01_-_I_Saw_Her_Standing_There.lab")
    assert(chord.length == 62)
    assert(chord.apply(2) == (11.459070,"A"))
  }
  test("testing read key data"){
    val key = keys("src/test/resources/01_-_It_Won't_Be_Long.lab")
    assert(key.apply(0) ==  (0.0,"Silence"))
    assert(key.apply(1) ==  (1.03.toFloat,"E"))
    assert(key.apply(2) == (129.94.toFloat,"Silence"))
    assert(key.length == 3)
  }
  test("enharmonic equivelants"){
    val chord = "Bb:maj7"
    val sharp = enharmonicEquivelent(chord)
    assert(sharp == "A#:maj7")

  }
}