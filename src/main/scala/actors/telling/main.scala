package actors.telling

import SieveActor.NumListMsg
import akka.actor.typed.ActorSystem

@main
def main(): Unit = {

  // create a system of actors with one root actor (sieve-1)
  val system = ActorSystem(SieveActor(null), "sieve-1")
  // HINT: root actor doesn't have a collector reference as it creates the collector actor

  // tell the system to compute a list of 100 integers
  system ! NumListMsg((1 to 100).toList)

}
