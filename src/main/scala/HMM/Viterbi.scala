package HMM

object Viterbi {

  //emms = P(), trans = P , init = P(chord)
  def viterbi(emms:Array[Array[Float]], trans:Array[Array[Float]], init:Array[(Float,Int)], obvs:Array[Int]) = {
    var alpha = Array.ofDim[(Float,Int)](obvs.length,25)
    alpha.update(0,init)

    for(i <- 1 until obvs.length) {
      alpha.update(i,currentBest(alpha(i-1), trans, emms, obvs(i)))
    }
    var arr = Array.ofDim[Int](obvs.length)
    var max = alpha.last.reduce((a, b) => if (a._1.>(b._1)) a else b)
    var index = alpha.last.indexOf(max)
    for (i <- 1 until obvs.length) {
      arr.update(obvs.length - i, index)
      index = max._2
      max = alpha(obvs.length - i - 1)(index)
    }
    arr
  }

  def currentBest(prev: Array[(Float,Int)], trans: Array[Array[Float]], emms:Array[Array[Float]], obv:Int):Array[(Float,Int)] = {
    val current = Array.fill[(Float,Int)](prev.length)((0,0))
    for (i <- 0 until prev.length - 1 ) {
      for (j <- 0 until prev.length -1 ) {
        val newtrans = prev(i)._1.*(trans(i)(j)).*(emms(j)(obv))
        if (newtrans.>(current(i)._1)) current.update(i, (newtrans,i))
      }
    }
    current
  }

}
