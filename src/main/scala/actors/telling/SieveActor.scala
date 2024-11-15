package actors.telling

import CollectorActor.{CollectMsg, CollectorMsg, FinishMsg}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object SieveActor {

  // sieve actor can only compute messages with a list of numbers
  case class NumListMsg(numbers: List[Int])

  def apply(collectorRef: ActorRef[CollectorMsg]): Behavior[NumListMsg] =
    Behaviors.receive { (context, msg) =>
      // if collector reference already exists, provide it
      var collectorAddress: ActorRef[CollectorMsg] = collectorRef
      // else spawn the collector actor (only initial)
      if ((collectorAddress eq null) && context.child("collector").isEmpty) {
        collectorAddress = context.spawn(CollectorActor(), "collector")
      }
      msg match {
        // process list of numbers for next prime
        case NumListMsg(a :: tail) =>
          collectorAddress ! CollectMsg(a) // tell the collector to collect the current prime
          var filteredList: List[Int] = Nil
          if (a eq 1) // skip filtering initially
            filteredList = tail
          else // filter multiples for current prime
            filteredList = tail.filter(_ % a != 0)
          val successorAddress: ActorRef[NumListMsg] = context.spawn(SieveActor(collectorAddress), s"sieve-$a")
          successorAddress ! NumListMsg(filteredList) // forward filtered list to next sieve instance
        // end process chain if list is empty
        case NumListMsg(Nil) =>
          collectorAddress ! FinishMsg
      }
      Behaviors.same
  }

}
