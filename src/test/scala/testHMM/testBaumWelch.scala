package testHMM

import org.scalatest.FunSuite
import HMM.BaumWelch._

abstract class testBaumWelch extends FunSuite {

  val emms = Array(Array(0.2,0.2,0.6),Array(0.5,0.3,0.2),Array(0.1,0.6,0.3))
  val trans = Array(Array(0.3,0.4,0.3),Array(0.2,0.4,0.4),Array(0.2,0.3,0.5))
  val init = Array(0.3,0.3,0.4)
  val obvs = Array(1,0,2)

  test("testing forwards algorithm") {

    val alg = forwards(emms,trans,init,obvs)
    val res = Array(Array(0.06,0.09,0.24),Array(0.0504,0.198,0.0522),Array(0.117288,0.0669012,0.108378))

    assert(alg.deep == res.deep)
  }

  test("testing backwards algorithm") {

    val alg = backwards(emms,trans,obvs)
    val x = backwardsAccumulate(emms,trans,2,Array(1,1,1))
    val res = Array(backwardsAccumulate(emms,trans,0,x),x,Array(1.0,1.0,1.0))

    assert(alg.deep == res.deep)
  }

  test("testing forwards accumulate") {

    val prev = Array(0.3,0.2,0.1)
    val obv = 1
    val alg = forwardAccumulate(prev,trans,emms,obv).map(x => x)
    val res = Array(0.09,0.207,0.396)

    assert(alg.deep == res.deep)

  }

  test("testing backwards accumulate") {

    val obv = 1
    val next = Array(0.3,0.2,0.4)
    val alg = backwardsAccumulate(emms,trans,obv,next)
    val res = Array(0.342,0.396,0.45)

    assert(alg.deep == res.deep)

  }

  test("testing finding the sequence") {
    val forwards = Array(Array(0.1,0.2,0.2),Array(0.1,0.4,0.2),Array(0.7,0.1,0.2))
    val backwards = Array(Array(0.4,0.1,0.3),Array(0.4,0.2,0.1),Array(0.3,0.3,0.3))

    val seq1 = mostProbableStates(forwards,backwards)
    val seq = Array(2,1,0)
    assert(seq.deep == seq1.deep)
  }
}
