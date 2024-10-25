package parellelism

import java.util.concurrent.ForkJoinPool
import scala.concurrent.Future

@main
def main(): Unit = {

  // 1) retrieve amount of cores from the CPU
  val coreAmount = Runtime.getRuntime.availableProcessors()
  println(s"There are $coreAmount cores available.")

  // 2) create a fork-join pool with as many threads as available processors
  val threadPool = ForkJoinPool(coreAmount) // (work-stealing Thread-Pool)

  // 3) create an array of integers (workload)
  var workload: Array[Int] = (1 to 30).toArray
  println(s"Initial array: ${workload.mkString(", ")}")

  // 4) create and kickoff task for the recursive mutable approach
  val task1 = RecursiveMutableTask(workload, 0, workload.length - 1)
  threadPool.invoke(task1)
  println(s"Processed array: ${workload.mkString(", ")}")

  // 5) reset array (workload)
  workload = (1 to 30).toArray

  // 6) create and kickoff task for the recursive immutable approach
  val task2 = RecursiveImmutableTask(workload)
  val resList = threadPool.invoke(task2)
  println(s"Processed list: ${resList.mkString(", ")}")

  // 7) reset array (workload)
  workload = (1 to 30).toArray
}
