package parallelism.performance

object TableUtils {

  private def padTimeColumn(str: String): String = str.padTo(12, ' ')
  private def padExecutionColumn(str: String): String = str.padTo(32, ' ')
  private def padThresholdColumn(str: String): String = str.padTo(9, ' ')
  private def padStealColumn(str: String): String = str.padTo(11, ' ')
  private def padTableWidth(str: String): String = str.padTo(75, '-')

  def printTableHead(): Unit = {
    println(s"${padTableWidth("Performance Review ")}")
    println(s"${padTimeColumn("time in ns")} | " +
      s"${padExecutionColumn("type of execution")} | " +
      s"${padThresholdColumn("threshold")} | " +
      s"${padStealColumn("steal count")} |")
    println(padTableWidth(""))
  }

  def printTableRow(time: String, exec: String, threshold: String, steals: String): Unit = {
    println(s"${padTimeColumn(time)} | " +
      s"${padExecutionColumn(exec)} | " +
      s"${padThresholdColumn(threshold)} | " +
      s"${padStealColumn(steals)} |")
  }
  
  def printTableEnd(): Unit = println(padTableWidth(""))
  
}
