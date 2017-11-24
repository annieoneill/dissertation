package generativeHarmoniser.src.main.scala.trainingData

import scala.io.{BufferedSource, Source}

object keyData {

  def file(filename:String): BufferedSource = Source.fromFile(filename)
  def lines(bufferedSource: BufferedSource) = bufferedSource.mkString.split('\n')
  def perLine(arr: Array[String]) = arr.map(line => line.split('\t'))
  def keyChanges(perLine:Array[Array[String]]) = perLine.map(line => (line.apply(0).toFloat,line.apply(line.length-1)))

  def keys(filename:String): Array[(Float, String)] = keyChanges(perLine(lines(file(filename))))
}