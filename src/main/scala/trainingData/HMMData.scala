package trainingData

import keyPrediction._
import midiFileReader._
import chordData._
import scala.math.pow

object HMMData {

  def normalisedData(filename:String):Array[(Boolean,Int,Double)] = dataFromTracks(filename).map(x => normalisedNote(x._1,x._2.toDouble))
    .filter(o => o.getOrElse(0,0,0) != (0,0,0))
    .map(o => o.get)

  def getNotesOn(time: Long, data:Array[(Boolean,Int,Double)]): Double = {
    val usefulData = data.filter(x => x._3<time)
    val allNotes = usefulData.map(x => x._2).distinct
    val notesOn = allNotes.map( x => (x,usefulData.count(t => t._2==x).%(2))).filter(x => x._2==1).map(x => x._1)
    val normalisedNotes = notesOn.map(x => x.%(12)).distinct
    updatedMap(normalisedNotes)
  }

  //creates the map of notes on at a given point
  def updatedMap(notes: Array[Int]): Double = {
    val x = notes.map(x => pow(2,x))
    val y = x.length match {
      case 0 => 0
      case 1 => x.last
      case _ => x.reduce((a,b) => a + b)
    }
    y
  }

  //gets the tick and the notes for each sample point
  def sample(filename: String): Set[(Long, Double)] = {
    val length = midiFileReader.getSeq(filename).getTickLength
    var x = Set.empty[(Long,Double)]
    val data = normalisedData(filename)
    for (a <- 1L until length./(1000) ) {
      x = x.+((a,getNotesOn(a.*(1000),data)))
    }
    x
  }

  //gets the previousChord, label and notes for each sample point
  def trainingValues(midi:String, labelsFile:String): List[(String, Double, String)] = {
    val data = sample(midi)
    val tickLength  = ticklength(midi)
    val labels = chords(labelsFile)
    val tickNotesLabel = data.map(x => (x._1,x._2,labelValue(labels,x._1.*(1000)./(tickLength)))).toList
    tickNotesLabel.map(current =>
      (normaliseChord(tickNotesLabel.find(y => y._1 == current._1-1).getOrElse(0L,current._2, "N")._3),current._2,normaliseChord(current._3)))
  }



}