package generativeHarmoniser.src.main.scala.trainingData

import scala.io.{BufferedSource, Source}

object chordData {

  def file(filename:String): BufferedSource = Source.fromFile(filename)
  def lines(bufferedSource: BufferedSource) = bufferedSource.mkString.split('\n')
  def chordInfo(arr: Array[String]) = arr.map(line => line.split(' '))
  def chordPairs(chordInfo: Array[Array[String]]) = chordInfo.map(arr => (arr.apply(0).toDouble,arr.apply(2).split('/').apply(0)))

  def chords(filename:String) = chordPairs(chordInfo(lines(file(filename))))

}
