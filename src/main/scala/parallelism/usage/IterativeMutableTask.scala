package parallelism.usage

import java.util.concurrent.RecursiveAction

class IterativeMutableTask(array: Array[String], startIdx: Int, endIdx: Int) extends RecursiveAction {

  override def compute(): Unit = {
    if ((endIdx - startIdx) eq 1) {
      if (array.isDefinedAt(endIdx)) {
        // process block of two elements
        array.update(startIdx, s"processed(block(${array(startIdx)}, ${array(endIdx)}))")
        array.update(endIdx, s"${array(endIdx)} was processed in a block")
      } else {
        // process single elements
        array.update(startIdx, s"processed(${array(startIdx)})")
      }
    } else {
      // split array iterative into blocks of two elements for next recursive executions
      val taskArray: Array[IterativeMutableTask] = Array.ofDim(array.length)
      for (i <- startIdx to endIdx by 2) {
        val freshTask = IterativeMutableTask(array, i, i + 1)
        freshTask.fork()
        // start parallel execution for new part of array with size two
        taskArray.update(i, freshTask)
      }
      // wait for completion of all sub-tasks (non-blocking as thread is suspended)
      taskArray.foreach(task => if (task != null) task.join())
    }
  }

}
