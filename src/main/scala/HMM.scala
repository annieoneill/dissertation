package generativeHarmoniser.src.main.scala

import javax.sound.midi.MidiMessage
import javax.sound.midi.Sequence._

import generativeHarmoniser.src.main.scala.keyPrediction._
import generativeHarmoniser.src.main.scala.midiFileReader._
import generativeHarmoniser.src.main.scala.trainingData.chordData._

object HMM {

  def normalisedData(filename:String):Array[(Boolean,Int,Double)] = dataFromTracks(filename).map(x => normalisedNote(x._1,x._2.toDouble))
    .filter(o => o.getOrElse(1,1,1)._2 == 76)
    .map(o => o.get)

  def getVector(time: Long, data:Array[(Boolean,Int,Double)] ): Map[Int, Int] = {
    val usefulData = data.filter(x => x._3>time)
    var map = Map(0->0,1->0,2->0,3->0,4->0,5->0,6->0,7->0,8->0,9->0,10->0,11->0,12->0)
    val allNotes = usefulData.map(x => x._2).distinct
    val notesOn = allNotes.map( x => usefulData.filter(t => t._2==x).length.%(2)).filter(x => x==1)
    val normalisedNotes = notesOn.map(x => x.%(12))
    updatedMap(map,normalisedNotes)
  }

  def updatedMap(map: Map[Int, Int], notes: Array[Int]):Map[Int,Int] = {
    var updatemap:Map[Int,Int] = map
    for(note <- notes) {
      updatemap = updatemap.updated(note,1)
    }
    updatemap
  }

  def sample(filename: String): Set[(Long, Map[Int, Int])] = {
    val length = midiFileReader.getSeq(filename).getTickLength
    var x = Set.empty[(Long,Map[Int,Int])]
    for (a <- 0 until length) {
      x = x.+((a,getVector(a,normalisedData(filename))))
    }
    x
  }

  def train(midi:String, labels:String): Set[(Long, Map[Int, Int], String)] = {
    val data = sample(midi)
    val tickLength  = ticklength(midi)
    data.map(x => (x._1,x._2,labelValue(labels,x._1./(tickLength))))
  }


}