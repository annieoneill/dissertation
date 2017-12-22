package HMM

import scala.collection.mutable
import trainingData.HMMData._

object Model {

  def numberOfHiddenStates = 25
  def numberOfEmmittedCombs = 8191

  def statesMap = mutable.HashMap(("N",0),("A",1),("A#",2),("B",3),("C",4),("C#",5),("D",6),("D#",7),("E",8),("F",9),("F#",10),("G",11),("G#",12),
    ("A:min",13),("A#:min",14),("B:min",15),("C:min",16),("C#:min",17),("D:min",18),("D#:min",19),("E:min",20),("F:min",21),("F#:min",22),("G:min",23),("G#:min",24))

  //P(Chord)
  def chordProbabilities = frequencies._1.map(x => x./(frequencies._1.sum))

  //P(chord|PrevChord)
  def transitionProbabilities = frequencies._2.map(x => x.map(y => y.toFloat.+(1)./(x.sum + x.length)))

  //P(Emmission|Chord)
  def emmissionProbabilities = frequencies._3.map(x => x.map(y => (y.toFloat.+(1))./(x.sum + x.length)))


  def trainingData = List(("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid", "src/test/resources/01_-_I_Saw_Her_Standing_There.lab")) //TODO

  def data: List[(String, Double, String)] = trainingData.flatMap(x => trainingValues(x._1,x._2))

  def frequencies: (Array[Float], Array[Array[Int]], Array[Array[Int]]) = {

    val chordFrequencies = Array(1F,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)

    val transmissions = Array.ofDim[Int](numberOfHiddenStates,numberOfHiddenStates)
    val emissions = Array.ofDim[Int](numberOfHiddenStates,numberOfEmmittedCombs)
    val d = data

    data.foreach(x => {
      val chord = statesMap(x._3)
      val prevChord = statesMap(x._1)
      val emission = x._2.toInt
      chordFrequencies(chord) = chordFrequencies(chord).+(1)
      transmissions(prevChord)(chord) = transmissions(prevChord)(chord).+(1)
      emissions(chord)(emission) = emissions(chord)(emission).+(1)
    })
    (chordFrequencies,transmissions,emissions)

  }
}
