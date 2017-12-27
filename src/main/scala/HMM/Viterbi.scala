package HMM

object Viterbi {

  //emms = P(Emmission|Chord), trans = P(chord|PrevChord) , init = P(chord)
  def viterbi(emms:Array[Array[Double]], trans:Array[Array[Double]], init:Array[(Double,Int)], obvs:Array[Int]): Array[Int] = {
    var alpha = Array.ofDim[(Double,Int)](obvs.length,25)
    alpha.update(0,init)

    for(i <- 1 until obvs.length) {
      alpha.update(i,currentBest(alpha(i-1), trans, emms, obvs(i)))
    }
    backtrack(alpha,obvs.length)
  }


  def currentBest(prev: Array[(Double,Int)], trans: Array[Array[Double]], emms:Array[Array[Double]], obv:Int):Array[(Double,Int)] = {
    val current = Array.fill[(Double,Int)](prev.length)((0,0))
    for (i <- 0 until prev.length ) { // previous
      for (j <- 0 until prev.length ) { // current
        val newtrans = prev(i)._1.*(trans(i)(j)).*(emms(j)(obv))
        if (newtrans.>(current(j)._1)) current.update(j, (newtrans,i))
      }
    }
    current
  }

  def backtrack(alpha:Array[Array[(Double,Int)]], obvsLength:Int): Array[Int] = {
    var max = alpha.last.reduce((a, b) => if (a._1.>(b._1)) a else b)
    var index = alpha.last.indexOf(max)
    var arr = Array.ofDim[Int](obvsLength)
    for (i <- 1 until arr.length) {
      arr.update(obvsLength - i, index)
      index = max._2
      max = alpha(obvsLength - i - 1)(index)
    }
    arr
  }

}
