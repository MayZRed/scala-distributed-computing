package actors.asking

import actors.asking.Entities.{Invoice, Payment}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object CheckoutActor {

  case class CheckoutMsg(infos: Payment)

  sealed trait CheckoutResponse
  case class CheckoutSuccessResponse(invoice: Invoice) extends CheckoutResponse
  case object CheckoutFailedResponse extends CheckoutResponse

  def apply(): Behavior[CheckoutMsg] = Behaviors.receiveMessage { msg =>
    // TODO: add functionality
    Behaviors.same
  }

}
