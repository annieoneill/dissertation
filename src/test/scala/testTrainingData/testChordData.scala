package testTrainingData

import java.io.File

import org.scalatest.FunSuite
import trainingData.chordData._

abstract class testChordData extends FunSuite {

  test("testing reading chord data"){
    val chord = chords("src/test/resources/01_-_I_Saw_Her_Standing_There.lab")
    assert(chord.length == 62)
    assert(chord.apply(2) == (11.459070,"A"))
  }

  test("enharmonic equivalents"){
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

  test("test training labels and midi files match") {
    val resources = new File("src/main/resources/training").listFiles()
    val midis = resources.filter(x => x.getName.contains("mid")).map(x => x.getName.substring(14,x.getName.length - 4).toLowerCase)
    val labels = resources.filter(x => x.getName.contains("lab")).map(x => x.getName.substring(5, x.getName.length - 4).toLowerCase)

    val labelsWithoutMidi = labels.filter(x => !midis.exists(y => y.contains(x)))
    val midisWithoutLabels = midis.filter(x => !labels.exists(y => y.contains(x)))

    assert(labelsWithoutMidi.length == 0)
    assert(midisWithoutLabels.length == 0)
  }

  test("test test labels and midi files match") {

    val resources = new File("src/main/resources/test").listFiles()
    val midis = resources.filter(x => x.getName.contains("mid")).map(x => x.getName.substring(14,x.getName.length - 4).toLowerCase)
    val labels = resources.filter(x => x.getName.contains("lab")).map(x => x.getName.substring(5, x.getName.length - 4).toLowerCase)

    val labelsWithoutMidi = labels.filter(x => !midis.exists(y => y.contains(x)))
    val midisWithoutLabels = midis.filter(x => !labels.exists(y => y.contains(x)))

    assert(labelsWithoutMidi.length == 0)
    assert(midisWithoutLabels.length == 0)
  }

}