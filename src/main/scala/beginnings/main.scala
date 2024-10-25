package beginnings

import beginnings.TutorialEnum.{Value1, Value2}

@main
def main(): Unit = {
  // STANDARD- & INFIX-NOTATION
  calculateInStandardNotation() // see method
  calculateInInfixNotation() // see method
  // CLASS INSTANTIATION (lookup: TutorialClass)
  val instance1 = new TutorialClass("One", 1) // standard instantiation
  val instance2 = TutorialClass("Two", 2) // short instantiation
  // print instances (DEBUG)
  println("-------------------------------------------------------\n")
  println(s"1st element: ${instance1.asString()}")
  println(s"2nd element: ${instance2.asString()}")
  // use APPLY METHOD (lookup: TutorialClass)
  val history1: (String, Int) = instance1.apply() // standard syntax
  val history2: (String, Int) = instance2() // short syntax
  // use UPDATE METHODS (lookup: TutorialClass)
  useUpdateMethodsOfClass(instance1, instance2) // see method
  // print changes (DEBUG)
  println(s"updated 1st element from $history1 to ${instance1.asString()}")
  println(s"updated 2nd element from $history2 to ${instance2.asString()}")
  // use VARIABLES as METHODS (lookup: TutorialClass)
  useVariableMethodsOfClass(instance1, instance2) // see method
  // print changes (DEBUG)
  println(s"reverted 1st element: ${instance1.asString()}")
  println(s"reverted 2nd element: ${instance2.asString()}")
  println("-------------------------------------------------------\n")
  // use FACTORY METHOD (lookup: TutorialTrait)
  useFactoryMethod() // see method
  // use DATASTRUCTURES and STREAM-API
  val intList = useSequenceListAndMap(instance1, instance2) // see method
  // use PATTERN MATCHING
  usePatternMatching(intList)
  usePatternMatching(List())
}

def calculateInStandardNotation(): Unit = {
  // use standard notation for calling (static) methods from object
  val addResult: Int = TutorialCalculator.add(1, 1) // explicit type assignment
  val subResult = TutorialCalculator.subtract(1, 1) // implicit type assignment
  // use 's' in front of Strings to use variables with $ notation
  println(s"1+1=$addResult")
  println(s"1-1=$subResult")
}

def calculateInInfixNotation(): Unit = {
  // use infix notation for calling (static) methods from object
  // additionally use ${} notation for method calls in Strings
  println(s"2*2=${TutorialCalculator multiply (2, 2)}")
  println(s"2/2=${TutorialCalculator divide (2, 2)}")
}

def useUpdateMethodsOfClass(in1: TutorialClass, in2: TutorialClass): Unit = {
  // use standard syntax for update
  in1.update("Four")
  in1.update(4)
  // use short syntax for update
  in2() = "Five"
  in2() = 5
  // HINT: functions can be dispatched by (param-)type
}

def useVariableMethodsOfClass(in1: TutorialClass, in2: TutorialClass): Unit = {
  // use standard notation for "method" call
  in1.setDescriptionTo("One")
  in1.setValueTo(1)
  // use infix notation for "method" call
  in2 setDescriptionTo "Two"
  in2 setValueTo 2
  // HINT: methods work with call-by-reference
}

def useFactoryMethod(): Unit = {
  // use standard syntax of factory method
  val first = TutorialTrait.apply(Value1)
  // use short syntax for factory method
  val second = TutorialTrait(Value2)
  println(s"1st index: ${first.index()}")
  println(s"2nd index: ${second.index()}")

  println("-------------------------------------------------------\n")
}

def useSequenceListAndMap(in1: TutorialClass, in2: TutorialClass): List[Int] = {
  // 1.1) create SEQUENCE
  val sequence = Seq(in1, in2)
  // 1.2) use standard FOR-LOOP
  for (el <- sequence) el.print()
  // 1.3) use standard FOREACH-LOOP
  sequence.foreach(el => el.print())
  // 1.4) get first element with head()-method
  println(s"1st element: ${sequence.head.asString()}")
  // 1.5) get element by index
  println(s"2nd element: ${sequence(1).asString()}")
  // 2.1) create LIST
  val list = List(in1, in2)
  // 2.2) use standard FOR-LOOP
  for (el <- list) println(el.asString())
  // 2.3) use standard FOREACH-LOOP
  list.foreach(el => println(el.asString()))
  // 2.4) get first element with head()-method
  println(s"1st element: ${list.head.asString()}")
  // 2.5) get element by index
  println(s"2nd element: ${list(1).asString()}")
  // 2.6) use map()-method
  val intList = list.map(_.value)
  // HINT: use _ instead of el => el
  println(s"mapped to integer list: $intList")
  // 3.1) create MAP value-by-value
  val map1 = Map(
    in1.desc -> in1.value,
    in2.desc -> in2.value
  )
  // 3.2) use BiConsumer FOR-LOOP
  for ((desc, value) <- map1) println(s"$desc -> $value")
  // 3.3) create MAP from LIST
  val map2 = Map().mapFactory.newBuilder.addAll(list
    .map(el => (el.desc, el.value))).result()
  // 3.4) use BiConsumer FOREACH-LOOP
  map2.foreach((desc, value) => println(s"$desc -> $value"))

  println("-------------------------------------------------------\n")
  intList // returns list of integers
}

def usePatternMatching(intList: List[Int]): Unit = {
  for (int <- intList) {
    int match
      case 1 => println("Pattern matched 1 to One.")
      case 2 => println("Pattern matched 2 to Two.")
      case _ => println("???")
  }
  intList match
    case List(0, x) => println(s"List with 1st element: 0 and 2nd element: $x.")
    case List(1, x) => println(s"List with 1st element: 1 and 2nd element: $x.")
    case List(_, _, _, _*) => println("Random list with at least 3 elements.")
    case List(_, _*) => println("Random list with at least 1 element.")
    case Nil => println("List with no elements.")
}
