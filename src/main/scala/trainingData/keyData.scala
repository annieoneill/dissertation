package trainingData

import scala.io.Source

object keyData {

  def keys(filename:String): Array[(Float, String)] = {

    val file = Source.fromFile(filename)
    val lines = file.mkString.split('\n')
    val perLine = lines.map(line => line.split('\t'))
    perLine.map(line => (line.apply(0).toFloat,line.apply(line.length-1)))

  }
}