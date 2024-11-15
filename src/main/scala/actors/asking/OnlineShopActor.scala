package actors.asking

import actors.asking.Entities.*
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object OnlineShopActor {

  sealed trait ShoppingMsg
  case class AddItemToCartMsg(user: User, amount: Int, itemId: Long) extends ShoppingMsg
  case class InitiateCheckoutMsg(user: User, paymentType: PaymentType) extends ShoppingMsg

  class CheckoutInvoice(invoice: Invoice)

  def apply(): Behavior[ShoppingMsg] = Behaviors.setup { context =>
    // TODO: add functionality
    internalFunction()
  }

  private def internalFunction(): Behavior[ShoppingMsg] =
    Behaviors.receiveMessagePartial {
      case AddItemToCartMsg(user, amount, itemId) =>
        // TODO: add functionality
        Behaviors.same
      case InitiateCheckoutMsg(user, paymentType) =>
        // TODO: add functionality
        Behaviors.same
    }

}
