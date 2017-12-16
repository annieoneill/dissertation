package HMM

import scala.collection.mutable

object Model {
  def numberOfHiddenStates = 25
  def numberOfEmmittedCombs = 4096
  def statesMap = mutable.HashMap(("N",0),("A",1),("A#",2),("B",3),("C",4),("C#",5),("D",6),("D#",7),("E",8),("F",9),("F#",10),("G",11),("G#",12),
    ("A:min",13),("A#:min",14),("B:min",15),("C:min",16),("C#:min",17),("D:min",18),("D#:min",19),("E:min",20),("F:min",21),("F#:min",22),("G:min",23),("G#:min",24))

}
