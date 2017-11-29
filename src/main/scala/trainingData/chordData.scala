package generativeHarmoniser.src.main.scala.trainingData

import scala.io.{BufferedSource, Source}

object chordData {

  def chordInfo(filename:String) = Source.fromFile(filename).mkString
    .split('\n').map(line => line.split(' '))

  //
  def chordPairs(chordInfo: Array[Array[String]]) = chordInfo.map(arr => (arr.apply(0).toDouble,arr.apply(2).split('/').apply(0)))

  def chords(filename:String): Array[(Double, String)] = chordPairs(chordInfo(filename))

  //finds the label at a given time
  def labelValue(filename:String, time:Long) = {
    chords(filename).filter(x => x._1.<(time)).last._2
  }

}
