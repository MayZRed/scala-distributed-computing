package parellelism

import java.util.concurrent.RecursiveTask

class RecursiveImmutableTask(array: Array[Int]) extends RecursiveTask[List[Int]] {

  override def compute(): List[Int] = {
    println(s"Starting execution for array: ${array.mkString(", ")}")
    val size: Int = array.length
    if (size == 1) {
      // reset single elements to zero
      println(s"Reset element with value: ${array(0)} to 0.")
      List(0)
    } else if (size == 2) {
      // transfer one from second to first element
      val first: Int = array(0)
      val second: Int = array.last
      println(s"Increment element with value: $first by 1.")
      println(s"Decrement element with value: $second by 1.")
      List(first + 1, second - 1)
    } else {
      // split array in half for next recursive execution
      val splitIndex: Int = Math.round(Math.floor(size / 2).toFloat)
      println(s"Splitting current task at index: $splitIndex.")
      val arrays: (Array[Int], Array[Int]) = array.splitAt(splitIndex)
      val firstHalf = RecursiveImmutableTask(arrays._1)
      val secondHalf = RecursiveImmutableTask(arrays._2)
      // start parallel execution for new (partial) split of array
      firstHalf.fork()
      secondHalf.fork()
      // wait for completion of both sub-tasks (non-blocking as thread is suspended)
      List.concat(firstHalf.join(), secondHalf.join()) // returns result List[Int]
    }
  }

}
