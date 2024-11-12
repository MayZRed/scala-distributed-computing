package parallelism.usage

import java.util.concurrent.RecursiveTask

class RecursiveImmutableTask(array: Array[String]) extends RecursiveTask[List[String]] {

  override def compute(): List[String] = {
    val size: Int = array.length
    if (size eq 1) {
      // process single elements
      List(s"processed(${array(0)})")
    } else if (size eq 2) {
      // process block of two elements
      List(s"processed(block(${array(0)}, ${array(1)}))")
    } else {
      // split array in half for next recursive executions
      val splitIndex: Int = Math.round(Math.floor(size / 2).toFloat)
      val arrays: (Array[String], Array[String]) = array.splitAt(splitIndex)
      val firstHalf = RecursiveImmutableTask(arrays._1)
      val secondHalf = RecursiveImmutableTask(arrays._2)
      // start parallel execution for new (partial) splits of array
      firstHalf.fork()
      secondHalf.fork()
      // wait for completion of both sub-tasks (non-blocking as thread is suspended)
      List.concat(firstHalf.join(), secondHalf.join()) // returns result List[Int]
    }
  }

}
