package parallelism.performance

import java.util.concurrent.ForkJoinPool
import scala.collection.View
import scala.util.Random

@main
def main(): Unit = {

  // 1) make the garbage collector observe the following actions
  System.gc()

  // 2) retrieve amount of cores from the CPU
  val coreAmount = Runtime.getRuntime.availableProcessors()
  println(s"There are $coreAmount cores available.\n")

  // 3) create a huge array of big integers (workload)
  val arrSize: Int = 1000
  val numSize: Int = 50
  val workload: Array[Array[BigInt]] = Array.ofDim(arrSize, 2)
  for (i <- 0 until arrSize) {
    workload(i).update(0, genLargeNumber(numSize))
    workload(i).update(1, genLargeNumber(numSize))
  }
  println(s"Exercise: Get fractions for ${arrSize * 2} numbers with $numSize digits each.\n")

  // 4) print table head
  TableUtils.printTableHead()

  // 5) solve problem and print stats to table
  // 5.1) iterative approach
  val duration: Long = IterativeBigFractionTask(workload)
  TableUtils.printTableRow(duration.toString, "Static-Iterative-Method", "none", "none")

  // 5.2) recursive fork-join approaches
  // 5.2.1) half amount of cores as thread amount
  var currThreadPool = ForkJoinPool(coreAmount / 2)
  var currExecutionDescription: String = getExecutionDescription(coreAmount / 2)
  executeWithDifferentThreshold(currThreadPool, currExecutionDescription, workload)

  // 5.2.2) exact amount of cores as thread amount
  currThreadPool = ForkJoinPool(coreAmount)
  currExecutionDescription = getExecutionDescription(coreAmount)
  executeWithDifferentThreshold(currThreadPool, currExecutionDescription, workload)

  // 5.2.3) double amount of cores as thread amount
  currThreadPool = ForkJoinPool(coreAmount * 2)
  currExecutionDescription = getExecutionDescription(coreAmount * 2)
  executeWithDifferentThreshold(currThreadPool, currExecutionDescription, workload)

  // 5.2.4) thread amount is one less than amount of cores (common)
  currThreadPool = ForkJoinPool.commonPool()
  currExecutionDescription = s"Common Fork-Join-Pool"
  executeWithDifferentThreshold(currThreadPool, currExecutionDescription, workload)

  // 6) print table end
  TableUtils.printTableEnd()
}

// functionality for big random number generation
def firstDigit: Char = Random.between(49, 58).toChar // excluding 0
def digit: Char = Random.between(48, 58).toChar // random digits
def genLargeNumber(length: Int): BigInt = {
  require(length > 0, "length must be strictly positive")
  val digits = View(firstDigit) ++ View.fill(length - 1)(digit)
  BigInt(digits.mkString)
}

// functionality for different cases of Thread-Pool-Executions
def getExecutionDescription(thAmount: Int): String = s"Fork-Join-Pool with $thAmount Threads"
def executeWithDifferentThreshold(thPool: ForkJoinPool, execDesc: String, wl: Array[Array[BigInt]]): Unit = {
  // execute current Thread-Pool-Configuration with threshold of 1, 10, 100 and 1000
  for (threshold <- View(1, 10, 100, 1000)) {
    val task = RecursiveBigFractionTask(wl, threshold)
    val duration = thPool.invoke(task)
    TableUtils.printTableRow(duration.toString, execDesc, threshold.toString, thPool.getStealCount.toString)
  }
}

