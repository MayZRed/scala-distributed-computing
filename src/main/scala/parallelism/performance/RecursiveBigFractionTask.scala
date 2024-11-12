package parallelism.performance

import com.github.kiprobinson.bigfraction.BigFraction

import java.util.concurrent.RecursiveTask

class RecursiveBigFractionTask(array: Array[Array[BigInt]], threshold: Int) extends RecursiveTask[Long] {

  override def compute(): Long = {
    val startTime: Long = System.nanoTime()
    val size: Int = array.length
    if (size <= threshold) {
      // just any computing intensive task
        for (i <- array.indices) {
          val currArr = array(i)
          if (currArr.isDefinedAt(0) && currArr.isDefinedAt(1)) {
            // just any computing intensive task
            val frac = BigFraction.valueOf(currArr(0), currArr(1))
            // HINT: maybe print here, if interested
          }
      }
    } else {
      // split array in half for next recursive executions
      val splitIndex: Int = Math.round(Math.floor(size / 2).toFloat)
      val arrays: (Array[Array[BigInt]], Array[Array[BigInt]]) = array.splitAt(splitIndex)
      val firstHalf = RecursiveBigFractionTask(arrays._1, threshold)
      val secondHalf = RecursiveBigFractionTask(arrays._2, threshold)
      // start parallel execution for new (partial) splits of array
      firstHalf.fork()
      secondHalf.fork()
      // wait for completion of both sub-tasks (non-blocking as thread is suspended)
      firstHalf.join()
      secondHalf.join()
    }
    // only process duration of root thread is relevant
    System.nanoTime() - startTime // returns process duration
  }

}
