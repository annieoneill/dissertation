package trainingData

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

  //length of a tick in seconds
  def ticklength(filename: String):Long = {
    val seq = getSeq(filename)
    val ticks = seq.getTickLength
    val microsecs = seq.getMicrosecondLength
    ticks.*(1000000)./(microsecs)
  }

  def processTrack(track: Track): Set[(MidiMessage,Long)] = {
    var midievents = Set.empty[(MidiMessage,Long)]
    for (i <- 0 until track.size()) {
      val message = track.get(i).getMessage
      midievents = if (message.toString.contains("FastShortMessage")) {
        midievents.+((message, track.get(i).getTick))
      } else {
        midievents
      }
    }
    midievents
  }
}