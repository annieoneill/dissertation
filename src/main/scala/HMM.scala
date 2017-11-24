package generativeHarmoniser.src.main.scala

import javax.sound.midi.MidiMessage

import generativeHarmoniser.src.main.scala.keyPrediction._
import generativeHarmoniser.src.main.scala.midiFileReader.dataFromTracks

object HMM {

  def data:Array[(MidiMessage,Long)] = dataFromTracks("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid")
  def normalisedData:Array[(Boolean,Int,Double)] = data.map(x => normalisedNote(x._1,x._2.toDouble))
    .filter(o => o.getOrElse(1,1,1)._2 == 76)
    .map(o => o.get)

  def getVector(time: Long, data:Array[(Boolean,Int,Double)] ) = {
    def usefulData = data.filter(x => x._3>time)
    var map = Map(0->0,1->0,2->0,3->0,4->0,5->0,6->0,7->0,8->0,9->0,10->0,11->0,12->0)
    def allNotes = usefulData.map(x => x._2).distinct
    def notesOn = allNotes.map( x => usefulData.filter(t => t._2==x).length.%(2)).filter(x => x==1)
    def normalisedNotes = notesOn.map(x => x.%(12))
    updatedMap(map,normalisedNotes)
  }

  def updatedMap(map: Map[Int, Int], ints: Array[Int]):Map[Int,Int] = {
    var updatemap:Map[Int,Int] = map
    for(int <- ints) {
      updatemap = updatemap.updated(int,updatemap.get(int).get.+(1))
    }
    updatemap
  }

}