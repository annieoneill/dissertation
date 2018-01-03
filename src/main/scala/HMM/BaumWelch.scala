package HMM

object BaumWelch {

  def forwards(emms:Array[Array[Double]], trans:Array[Array[Double]], init:Array[Double], obvs:Array[Int]): Array[Array[Double]] = {
    var alpha = Array.ofDim[Double](obvs.length,25)
    for (x <- 0 until init.length) init.update(x,init(x).*(emms(x)(obvs(0))))
    alpha.update(0,init)

    for(i <- 1 until obvs.length) {
      alpha.update(i,forwardAccumulate(alpha(i-1), trans, emms, obvs(i)))
    }
    alpha
  }

  def forwardAccumulate(prev: Array[Double], trans: Array[Array[Double]], emms:Array[Array[Double]], obv:Int):Array[(Double)] = {
    val current = Array.fill[Double](prev.length)((0))
    for (i <- 0 until prev.length ) { //previous
      for (j <- 0 until prev.length ) { //current
        val newtrans = prev(i).*(emms(0).length).*(trans(i)(j)).*(emms(j)(obv))
        current.update(j, current.apply(j).+(newtrans))
      }
    }
    current
  }

  def backwards(emms:Array[Array[Double]], trans:Array[Array[Double]], obvs:Array[Int]): Array[Array[Double]] = {
    val beta = Array.ofDim[Double](obvs.length,emms.length)
    val init = Array.fill[Double](emms.length)(1)
    beta.update(beta.length - 1,init)

    for (i <- obvs.length -2 until -1 by -1 ) {
      beta.update(i,backwardsAccumulate(emms,trans,obvs(i+1),beta(i+1)))
    }
    beta
  }

  def backwardsAccumulate(emms:Array[Array[Double]], trans:Array[Array[Double]], obv:Int, next:Array[Double]): Array[Double] = {
    val current = Array.fill[Double](emms.length)(0)
    for (i <- 0 until emms.length) { //current
      for (j <- 0 until emms.length) { //next
        val newTrans = next(j).*(emms.apply(0).length).*(trans(i)(j)).*(emms(j)(obv))
        current.update(i,current.apply(i).+(newTrans))
      }
    }
    current
  }

  def mostProbableStates(forwards:Array[Array[Double]],backwards:Array[Array[Double]]) = {
    var arr = Array.ofDim[Double](forwards.length, forwards(0).length)
    val fb = for (i <- 0 until forwards.length) {
      for (j <- 0 until forwards(0).length)
        arr(i).update(j, forwards(i)(j).*(backwards(i)(j)))
    }
    val seq = arr.map(x => x.indexOf(x.reduce((a,b) => if (a.>(b)) a else b)))
    seq
  }

}
