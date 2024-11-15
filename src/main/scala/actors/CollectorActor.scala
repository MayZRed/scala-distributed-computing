package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable.ArrayBuffer

object CollectorActor {

  sealed trait CollectorMsg
  // message for collecting another prime
  case class CollectMsg(prime: Int) extends CollectorMsg
  // message for shutting down the actor system
  case object FinishMsg extends CollectorMsg

  // adapt public method to secure the ArrayBuffer is initialized empty
  def apply(): Behavior[CollectorMsg] = internalFunction(ArrayBuffer.empty)

  private def internalFunction(primes: ArrayBuffer[Int]): Behavior[CollectorMsg] =
    Behaviors.receive { (context, msg) =>
      msg match {
        case CollectMsg(prime) =>
          // store a received prime in the ArrayBuffer
          primes.addOne(prime)
          // return a stateful behavior
          internalFunction(primes)
        case FinishMsg =>
          primes.sorted
          // provide the result (collected primes)
          println(s"\nResult - collected following ${primes.length} prime numbers:")
          println(s"[${primes.mkString(", ")}]\n")
          // shut down the actor system and stop behavior
          context.system.terminate()
          Behaviors.stopped
      }
  }

}
