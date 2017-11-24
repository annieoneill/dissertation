package generativeHarmoniser.src.main.scala

import java.io.File
import javax.sound.midi._

object midiFileReader {

  def getSeq(filename: String):Sequence = {
    MidiSystem.getSequence(new File(filename))
  }

  def dataFromTracks(filename: String):Array[(MidiMessage,Long)]= {
    val tracks  = getSeq(filename).getTracks
    tracks.flatMap(x => processTrack(x))
  }

  def midiLength(filename: String):(Long,Float,Int) = {
    val seq = getSeq(filename)
    val ticks = seq.getTickLength
    val resolution = seq.getResolution
    val divType = seq.getDivisionType
    (ticks,divType, resolution)
  }

  def processTrack(track: Track): Set[(MidiMessage,Long)] = {
    var midievents = Set.empty[(MidiMessage,Long)]
    for (i <- 0 to track.size()-1) {
      val message = track.get(i).getMessage
      midievents = message.toString.contains("FastShortMessage") match {
        case true => midievents.+((message, track.get(i).getTick))
        case false => midievents
      }
    }
    midievents
  }
}