package actors

import actors.CollectorActor.{CollectMsg, CollectorMsg, FinishMsg}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object SieveActor {

  case class NumListMsg(numbers: List[Int])

  def apply(collectorRef: ActorRef[CollectorMsg]): Behavior[NumListMsg] =
    Behaviors.receive { (context, msg) =>
      var collectorAddress: ActorRef[CollectorMsg] = collectorRef
      if (collectorAddress eq null) {
        collectorAddress = context.spawn(CollectorActor(), "collector")
      }
      msg match {
        case NumListMsg(a :: tail) => {
          collectorAddress ! CollectMsg(a)
          var filteredList: List[Int] = Nil
          if (a eq 1)
            filteredList = tail
          else
            filteredList = tail.filter(_ % a != 0)
          val nextSieveAddress: ActorRef[NumListMsg] = context.spawn(SieveActor(collectorAddress), s"sieve-$a")
          nextSieveAddress ! NumListMsg(filteredList)
        }
        case NumListMsg(optList) =>
          if (optList.isEmpty)
            collectorAddress ! FinishMsg
      }
      Behaviors.same
    }

}
