package beginnings

class TutorialClass(var desc:String, var value:Int) {
  
  def apply(): (String, Int) = (desc, value)
  
  def update(str:String): Unit = desc = str
  def update(int:Int): Unit = value = int

  def asString(): String = s"($desc, $value)"
  def print(): Unit = println(s"$desc -> $value")
  
  val setDescriptionTo: String => Unit = (str:String) => desc = str
  val setValueTo: Int => Unit = (num:Int) => value = num

}
