package actors

import actors.SieveActor.NumListMsg
import akka.actor.typed.ActorSystem

@main
def main(): Unit = {

  val system = ActorSystem(SieveActor(null), "sieve-1")
  system ! NumListMsg((1 to 100).toList)

}
