package parallelism.usage

import java.util.concurrent.ForkJoinPool

@main
def main(): Unit = {

  // 1) retrieve amount of cores from the CPU
  val coreAmount = Runtime.getRuntime.availableProcessors()
  println(s"There are $coreAmount cores available.")

  // 2) create a fork-join pool with as many threads as available processors
  val threadPool = ForkJoinPool(coreAmount) // (work-stealing Thread-Pool)

  // 3) create an array of strings (workload)
  val size: Int = 15
  val workload: Array[String] = Array.ofDim(size) // initialization
  for (i <- 0 until size) workload.update(i, s"val$i") // preparation
  println(s"Initial array: [${workload.mkString(", ")}] \n")

  // 4) create and kickoff task for the iterative mutable approach
  val task3 = IterativeMutableTask(workload, 0, workload.length - 1)
  threadPool.invoke(task3) // kickoff
  println(s"Processed array: (iterative mutable)\n - ${workload.mkString("\n - ")}\n")
  for (i <- 0 until size) workload.update(i, s"val$i") // reset

  // 5) create and kickoff task for the recursive mutable approach
  val task1 = RecursiveMutableTask(workload, 0, workload.length - 1)
  threadPool.invoke(task1) // kickoff
  println(s"Processed array: (recursive mutable)\n - ${workload.mkString("\n - ")}\n")
  for (i <- 0 until size) workload.update(i, s"val$i") // reset

  // 6) create and kickoff task for the recursive immutable approach
  val task2 = RecursiveImmutableTask(workload)
  val resList = threadPool.invoke(task2) // kickoff
  println(s"Processed list: (recursive immutable)\n - ${resList.mkString("\n - ")}\n")
  // HINT: workload isn't changed within this approach
}
