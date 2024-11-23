package actors.asking

import actors.asking.CheckoutActor.CheckoutMsg
import actors.asking.Entities.*
import actors.asking.ShoppingCartActor.{AddItemForUserMsg, GetOrderForUserMsg, ShoppingCartMsg, TestResponseMsg}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.util.Timeout

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.util.Success
import scala.util.Failure

object OnlineShopActor {

  sealed trait ShoppingMsg
  // request messages
  case class AddItemMsg(user: User, amount: Int, itemId: Long) extends ShoppingMsg
  case class PlaceOrderMsg(user: User) extends ShoppingMsg
  // response messages
  case class CheckoutInvoice(invoice: Invoice) extends ShoppingMsg
  case class ActionSuccessMsg(info: String) extends ShoppingMsg
  case class ActionFailedMsg(info: String) extends ShoppingMsg

  def apply(): Behavior[ShoppingMsg] = Behaviors.setup { ctx =>
    println("\nSTARTING the OnlineShop...\n")
    // set up the root ShoppingCartActor and the CheckoutActor
    val shoppingCartRef: ActorRef[ShoppingCartMsg] = ctx.spawn(ShoppingCartActor(), "shopping-cart-router")
    val checkoutRef: ActorRef[CheckoutMsg] = ctx.spawn(CheckoutActor(), "checkout-actor")
    incomingFunction(shoppingCartRef, checkoutRef)
  }

  private def incomingFunction(shoppingCartRef: ActorRef[ShoppingCartMsg], checkoutRef: ActorRef[CheckoutMsg]): 
    Behavior[ShoppingMsg] = Behaviors.receive { (ctx, msg) => {
      // set upper limit for response time (timeout)
      implicit val timeout: Timeout = Timeout(FiniteDuration(5, TimeUnit.SECONDS))

      msg match
       // process ADD-ITEM action
       case AddItemMsg(user, amount, itemId) =>
         ctx.ask(shoppingCartRef, ref => AddItemForUserMsg(user.getId, amount, itemId, ref)) {
           case Success(res) =>
             res match
               case TestResponseMsg(desc) => ActionSuccessMsg(desc)
               // HINT: there are no other response possibilities
           case Failure(ex) =>
             ActionFailedMsg(
               s"Adding item with ID: $itemId for user with ID: ${user.getId} failed due to: ${ex.getMessage}")
         }
       // process PLACE-ORDER action
       case PlaceOrderMsg(user) =>
         ctx.ask(shoppingCartRef, ref => GetOrderForUserMsg(user, checkoutRef, ref)) {
           case Success(res) =>
             res match
               case CheckoutInvoice(invoice) => CheckoutInvoice(invoice)
               case ActionSuccessMsg(text) => ActionSuccessMsg(text)
               case ActionFailedMsg(text) => ActionFailedMsg(text)
           case Failure(ex) =>
             ActionFailedMsg(s"Placing an order for user with ID: ${user.getId} failed due to: ${ex.getMessage}")
         }
       // process RESPONSE messages
       case CheckoutInvoice(invoice) => invoice.printInvoice()
       case ActionSuccessMsg(text) => println(text)
       case ActionFailedMsg(text) => println(Console.RED + s"\n$text" + Console.RESET)
       // TODO: maybe send invoice as response to main (ask)
      Behaviors.same
    }
  }

}
