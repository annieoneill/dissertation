package trainingData

import scala.io.Source

object chordData {

  def chords(filename:String): Array[(Double, String)] = {
    val chordInfo = Source.fromFile(filename).mkString.split('\n').map(line => line.split(' '))
    chordInfo.map(arr => (arr.apply(0).toDouble,arr.apply(2).split('/').apply(0)))
  }

  //finds the label at a given time
  def labelValue(chords: Array[(Double,String)], time:Long): String = {
    chords.filter(x => x._1.<(time)).last._2
  }

  def normaliseChord(chordName: String):String = {
    val chord = if (chordName.contains("aug")) {
      chordName.replace("aug", "")
    } else if (chordName.contains("dim")) {
      chordName.replace("dim","min")
    }
      else {
      chordName
    }
    if (chord.length.>(1))  {
      if (chord.charAt(1) == 'b') {
        removeNumbers(enharmonicEquivelent(chord))
      }
      else removeNumbers(chord)
    }
    else {
      removeNumbers(chord)
    }
  }

  def removeNumbers(chordName: String):String = {
    val min = chordName.contains("min")
    val sharp = (chordName.length.>(1)).&&(chordName.charAt(1) == '#')
    val returnLength = (min,sharp) match {
      case (true, true) => 6
      case (true, false) => 5
      case (false, true) => 2
      case (false, false) => 1
    }
    chordName.substring(0,returnLength)
  }

  def enharmonicEquivelent(flatChord: String):String = {
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
