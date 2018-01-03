package trainingData

import scala.io.Source

object chordData {

  def chords(filename:String): Array[(Double, String)] = {
    val chordInfo = Source.fromFile(filename).mkString.split('\n').map(line => line.split(' ').flatMap(l => l.split('\t')))
    chordInfo.map(arr => (arr.apply(0).toDouble,arr.apply(2).split('/').apply(0)))
  }

  //finds the label at a given time
  def labelValue(chords: Array[(Double,String)], time:Long): String = {
    var chord = chords.filter(x => x._1.<(time))
    if (chord.length.>(0)) chord.last._2
    else "N"
  }

  def normaliseChord(chordName: String):String = {
    var chord = chordName

    if (chord.contains("aug")) {
      chord = chord.replace("aug", "")
    }
    if (chord.contains("dim")) {
      chord = chord.replace("dim","min")
    }
    if (chord.contains("hmin")) {
      chord = chord.replace("hmin","min")
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
