package generativeHarmoniser.src.main.scala.trainingData

import scala.io.{BufferedSource, Source}

object chordData {

  def chords(filename:String): Array[(Double, String)] = {

    val chordInfo = Source.fromFile(filename).mkString.split('\n').map(line => line.split(' '))
    chordInfo.map(arr => (arr.apply(0).toDouble,arr.apply(2).split('/').apply(0)))

  }

  //finds the label at a given time
  def labelValue(chords: Array[(Double,String)], time:Long): String = {
    chords.filter(x => x._1.<(time)).last._2
  }

  def normaliseChord(chordName: String) = {
    val chord = if (chordName.contains("aug")) {
      chordName.replace("aug", "")
    } else if (chordName.contains("dim")) {
      chordName.replace("dim","")
    }
      else {
      chordName
    }
    if (chord.contains("b")) {
      chord
    } else {
      enharmonicEquivelent(chord)
    }
  }

  def enharmonicEquivelent(flatChord: String) = {
    val equiv = flatChord.substring(0,2) match {
      case "Ab" => "G#"
      case "Bb" => "A#"
      case "Cb" => "B#"
      case "Db" => "C#"
      case "Eb" => "D#"
      case "Fb" => "E#"
      case "Gb" => "F#"
    }
    flatChord.replace(flatChord.substring(0,2),equiv)
  }

}
