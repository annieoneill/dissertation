package generativeHarmoniser.src.main.scala

import generativeHarmoniser.src.main.scala.keyPrediction._
import generativeHarmoniser.src.main.scala.midiFileReader._
import generativeHarmoniser.src.main.scala.trainingData.chordData._

object HMMData {

  def normalisedData(filename:String):Array[(Boolean,Int,Double)] = dataFromTracks(filename).map(x => normalisedNote(x._1,x._2.toDouble))
    .filter(o => o.getOrElse(0,0,0) != (0,0,0))
    .map(o => o.get)

  def getVector(time: Long, data:Array[(Boolean,Int,Double)] ): Map[Int, Int] = {
    val usefulData = data.filter(x => x._3<time)
    val map = Map(0->0,1->0,2->0,3->0,4->0,5->0,6->0,7->0,8->0,9->0,10->0,11->0,12->0)
    val allNotes = usefulData.map(x => x._2).distinct
    val notesOn = allNotes.map( x => (x,usefulData.count(t => t._2==x).%(2))).filter(x => x._2==1).map(x => x._1)
    val normalisedNotes = notesOn.map(x => x.%(12))
    updatedMap(map,normalisedNotes)
  }

  //creates the map of notes on at a given point
  def updatedMap(map: Map[Int, Int], notes: Array[Int]):Map[Int,Int] = {
    var updatemap:Map[Int,Int] = map
    for(note <- notes) {
      updatemap = updatemap.updated(note,1)
    }
    updatemap
  }

  //gets the tick and the notes for each sample point
  def sample(filename: String): Set[(Long, Map[Int, Int])] = {
    val length = midiFileReader.getSeq(filename).getTickLength
    var x = Set.empty[(Long,Map[Int,Int])]
    val data = normalisedData(filename)
    for (a <- 1L until length./(1000) ) {
      x = x.+((a,getVector(a.*(1000),data)))
    }
    x
  }

  //gets the previousChord, label and notes for each sample point
  def trainingValues(midi:String, labelsFile:String): Set[(String, Map[Int, Int], String)] = {
    val data = sample(midi)
    val tickLength  = ticklength(midi)
    val labels = chords(labelsFile)
    val tickLNotesLabel = data.map(x => (x._1,x._2,labelValue(labels,x._1.*(1000)./(tickLength))))
    tickLNotesLabel.map(current =>
      (tickLNotesLabel.find(y => y._1 == current._1-1).getOrElse(0L,current._2, "N")._3,current._2,current._3))

  }



}