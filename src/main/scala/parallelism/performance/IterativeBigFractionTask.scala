package parallelism.performance

import com.github.kiprobinson.bigfraction.BigFraction

object IterativeBigFractionTask {

  def apply(array: Array[Array[BigInt]]): Long = {
    val startTime: Long = System.nanoTime() // save starting time
    for (i <- array.indices) {
      val currArr = array(i)
      if (currArr.isDefinedAt(0) && currArr.isDefinedAt(1)) {
        // just any computing intensive task
        val frac = BigFraction.valueOf(currArr(0), currArr(1))
        // HINT: maybe print here, if interested
      }
    }
    System.nanoTime() - startTime // returns process duration
  }

}
