package testTrainingData

import org.scalatest.FunSuite
import trainingData.chordData._

class testChordData extends FunSuite {

  test("testing reading chord data"){
    val chord = chords("src/test/resources/01_-_I_Saw_Her_Standing_There.lab")
    assert(chord.length == 62)
    assert(chord.apply(2) == (11.459070,"A"))
  }

  test("enharmonic equivelants"){
    val chord = "Bb:maj7"
    val sharp = enharmonicEquivelent(chord)
    assert(sharp == "A#:maj7")
  }

  test("test normalising data"){
    val test1 = normaliseChord("Bb:aug7")
    val test2 = normaliseChord("C#:dim5")
    val test3 = normaliseChord("E:7")
    assert(test1 == "A#")
    assert(test3 == "E")
    assert(test2 == "C#:min")
  }
}