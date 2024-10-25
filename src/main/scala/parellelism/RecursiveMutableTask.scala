package parellelism

import java.util.concurrent.RecursiveAction

class RecursiveMutableTask(array: Array[Int], startIdx: Int, endIdx: Int) extends RecursiveAction {

  override def compute(): Unit = {
    println(s"Starting execution from index: $startIdx to index: $endIdx.")
    val diff: Int = endIdx - startIdx
    if (diff == 0) {
      // reset single elements to zero
      array.update(startIdx, 0)
      println(s"Reset element with index: $startIdx to 0.")
    } else if (diff == 1) {
      // transfer one from second to first element
      array.update(startIdx, array(startIdx) + 1)
      println(s"Incremented element with index: $startIdx by 1.")
      array.update(endIdx, array(endIdx) - 1)
      println(s"Decremented element with index: $endIdx by 1.")
    } else {
      // split array in half for next recursive execution
      val splitIndex: Int = Math.round(Math.floor((diff / 2) + startIdx).toFloat)
      println(s"Splitting current task at index: $splitIndex.")
      val firstHalf = RecursiveMutableTask(array, startIdx, splitIndex)
      val secondHalf = RecursiveMutableTask(array, splitIndex + 1, endIdx)
      // start parallel execution for new (partial) split of array
      firstHalf.fork()
      secondHalf.fork()
      // wait for completion of both sub-tasks (non-blocking as thread is suspended)
      firstHalf.join()
      secondHalf.join()
    }
  }

}
