package generativeHarmoniser.src.main.scala

import javax.sound.midi.{MidiMessage, ShortMessage}

object keyPrediction {

  def normalisedNote(message:MidiMessage, tick:Double): Option[(Boolean, Int, Double)] = {

    val sm: ShortMessage = message.asInstanceOf[ShortMessage]
    val note = sm.getData1
    val ret = if (sm.getCommand.<(0xA0)) {
      Some(sm.getData2 != 0, note, tick)
    } else {
      None
    }
    ret
  }

  def wholePieceVector(notes:Array[(Boolean,Int,Double)]): Map[Int, Double] = {

    // notes = noteOn?,note,tick
    val on = notes.filter(x => x._1)
    val off = notes.filter(x => !x._1)

    val noteLengths: Array[(Int, Double)] = on.map(x => Tuple2(x._2.%(12), nextOff(x._2,x._3,off) - x._3))

    def value(i:Int): Double = noteLengths.filter(x => x._1 == i).map(x => x._2).sum
    val noteMap:Map[Int,Double] = Map(0->value(0),1->value(1),2->value(2),3->value(3),
      4->value(4),5->value(5),6->value(6),7->value(7),8->value(8),9->value(9),10->value(10),11->value(11))
    noteMap

  }

  def nextOff(note:Int, tick:Double, off:Array[(Boolean, Int, Double)]):Double = {
    off.filter(x => (x._2 == note)&&(x._3 > tick)).apply(0)._3
  }
}