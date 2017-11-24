package generativeHarmoniser.src.test.scala

import java.io.File
import javax.sound.midi._

import org.scalatest.FunSuite
import generativeHarmoniser.src.main.scala.midiFileReader._
import generativeHarmoniser.src.main.scala.keyPrediction._


class testMidiFileReader extends FunSuite {


  def data = dataFromTracks("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid")
  def seq = MidiSystem.getSequence(new File("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid"))
  def normalisedData = data.map(x => normalisedNote(x._1,x._2.toDouble)).filter(o => o.getOrElse(1,1,1)._2 == 76).map(o => o.get)

  test("testing reading midi files") {

    def offs = normalisedData.filter(x => x._3 == 0).length
    def ons = normalisedData.filter(x => x._3 != 0).length

    assert(offs == ons)
    assert(offs != 0)
  }

  test("testing data read from file is as it should be") {

    def file = new File("src/test/resources/outputMidi.mid")
    var newSeq = new Sequence(javax.sound.midi.Sequence.PPQ,24)
    var track = newSeq.createTrack()
    var sm1 = new ShortMessage()
    var sm2 = new ShortMessage()
    var mm1 = new MetaMessage()
    sm1.setMessage(0x90,1,1,1)
    sm2.setMessage(0x90,2,2,2)
    mm1.setMessage(0x2F,Array.emptyByteArray,0)

    val b = Array(0xF0.toByte, 0x7E.toByte, 0x7F.toByte, 0x09.toByte, 0x01.toByte, 0xF7.toByte)
    val sm = new SysexMessage()
    sm.setMessage(b, 6)

    def midievent4 = new MidiEvent(sm,0)
    def midievent1 = new MidiEvent(sm1,10)
    def midievent2 = new MidiEvent(sm2,20)
    def midievent3 = new MidiEvent(mm1,140)
    track.add(midievent4)

    track.add(midievent1)
    track.add(midievent2)
    track.add(midievent3)


    assert(newSeq.getTracks.length == 1)

    MidiSystem.write(newSeq,1,file)

    def readMidi = dataFromTracks("src/test/resources/outputMidi.mid")
      .map(x => normalisedNote(x._1,x._2.toDouble))
      .map(o => o.get)

    assert(readMidi.length == 2)
    assert(readMidi.apply(0) == (true,1,10))
    assert(readMidi.apply(1) == (true,2,20))
  }

  test("test midi file length is same right") {
    def lengths = midiLength("src/test/resources/The_Beatles_-_I_Saw_Her_Standing_There.mid")

    assert(lengths._1 == 55199)
    assert(lengths._2 == Sequence.PPQ)
    assert(lengths._3 == 120)
  }

}