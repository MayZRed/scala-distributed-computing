package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable.ArrayBuffer

object CollectorActor {

  sealed trait CollectorMsg
  case class CollectMsg(prime: Int) extends CollectorMsg
  case object FinishMsg extends CollectorMsg

  def apply(): Behavior[CollectorMsg] = internalFunction(ArrayBuffer.empty)

  private def internalFunction(primes: ArrayBuffer[Int]): Behavior[CollectorMsg] = Behaviors.receive {
    (context, msg) =>
      msg match {
        case CollectMsg(prime) =>
          primes.addOne(prime)
          internalFunction(primes)
        case FinishMsg =>
          primes.sorted
          println(s"Collected following ${primes.length} prime numbers:")
          println(primes.mkString(", "))
          context.system.terminate()
          Behaviors.stopped
      }
  }

}
