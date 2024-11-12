package parallelism.usage

import java.util.concurrent.RecursiveAction

class RecursiveMutableTask(array: Array[String], startIdx: Int, endIdx: Int) extends RecursiveAction {

  override def compute(): Unit = {
    val diff: Int = endIdx - startIdx
    if (diff eq 0) {
      // process single elements
      array.update(startIdx, s"processed(${array(startIdx)})")
    } else if (diff eq 1) {
      // process block of two elements
      array.update(startIdx, s"processed(block(${array(startIdx)}, ${array(endIdx)}))")
      array.update(endIdx, s"${array(endIdx)} was processed in a block")
    } else {
      // determine middle index for next recursive executions
      val splitIndex: Int = Math.round(Math.floor((diff / 2) + startIdx).toFloat)
      val firstHalf = RecursiveMutableTask(array, startIdx, splitIndex)
      val secondHalf = RecursiveMutableTask(array, splitIndex + 1, endIdx)
      // start parallel execution for new indexed parts of array
      firstHalf.fork()
      secondHalf.fork()
      // wait for completion of both sub-tasks (non-blocking as thread is suspended)
      firstHalf.join()
      secondHalf.join()
    }
  }

}
