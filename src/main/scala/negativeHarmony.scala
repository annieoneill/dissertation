package negative

object negativeHarmony {

  def invertNotes(notes:Array[Int], tonic:Int):Array[Int] = {
    notes.map(2 * tonic - 5 - _)
  }
}
