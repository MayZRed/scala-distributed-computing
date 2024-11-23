package actors.asking

import actors.asking.CheckoutActor.CheckoutMsg
import actors.asking.Entities.*
import actors.asking.OnlineShopActor.{ActionSuccessMsg, ShoppingMsg}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable

object ShoppingCartActor {

  sealed trait ShoppingCartMsg(userId: Long) {
    // ID of user is required for routing purposes
    def getUserId: Long = userId
  }
  
  case class TestResponseMsg(desc: String) // for test purpose regarding 'ask' with different reference
  
  case class AddItemForUserMsg(userId: Long, amount: Int, itemId: Long, callbackTo: ActorRef[TestResponseMsg])
    extends ShoppingCartMsg(userId)
  case class GetOrderForUserMsg(user: User, forwardTo: ActorRef[CheckoutMsg], callbackTo: ActorRef[ShoppingMsg])
    extends ShoppingCartMsg(user.getId)
  private case class CleanDirectoryMsg(userId: Long) extends ShoppingCartMsg(userId)

  class OrderForUserResponse(userId: Long, order: List[OrderItem])

  // adapt public method to ensure the ShoppingCart(Router) is initialized empty
  def apply(): Behavior[ShoppingCartMsg] = routingFunction(mutable.Map.empty)

  // partly simulate behaviour of a pool router for ShoppingCarts of different users
  private def routingFunction(directory: mutable.Map[Long, ActorRef[ShoppingCartMsg]]): Behavior[ShoppingCartMsg] =
    Behaviors.receive { (ctx, msg) => {
      if (msg.isInstanceOf[CleanDirectoryMsg]) {
        // 0) remove directory entry for stopped ShoppingCartActor
        directory.remove(msg.getUserId)
        routingFunction(directory) // return cleaned directory
      } else {
        val dedicatedUserId: Long = msg.getUserId
        val dedicatedCartName: String = s"shopping-cart-$dedicatedUserId"
        var dedicatedCartRef: ActorRef[ShoppingCartMsg] = null

        val optReferenceFromDirectory = directory.get(dedicatedUserId)
        if (optReferenceFromDirectory.isDefined && ctx.child(dedicatedCartName).isDefined) {
          // 1) forward message to already existing ShoppingCartActor for current user
          dedicatedCartRef = optReferenceFromDirectory.get
          dedicatedCartRef ! msg
          routingFunction(directory) // return directory unchanged
        } else {
          // 2) create fresh ShoppingCartActor for current user and forward message to it
          dedicatedCartRef = ctx.spawn(contentFunction(ctx.self, mutable.Map.empty), dedicatedCartName)
          dedicatedCartRef ! msg
          directory.addOne(dedicatedUserId, dedicatedCartRef)
          routingFunction(directory) // return directory with new entry
        }
      }
    }
  }

  // define core functionality for working instances of the ShoppingCartActor
  private def contentFunction(routerRef: ActorRef[ShoppingCartMsg], order: mutable.Map[Long, OrderItem]):
    Behavior[ShoppingCartMsg] = Behaviors.receiveMessagePartial {
      case AddItemForUserMsg(userId, amount, itemId, callbackRef) =>
        // 1) add amount of items to ShoppingCart of current user
        if (order.contains(itemId)) {
          // 1.1) only update amount of an already ordered item
          val orderedItem: OrderItem = order.remove(itemId).get
          order.addOne(itemId, OrderItem(orderedItem.getAmount + amount, orderedItem.getItem))
        } else {
          // 1.2) add new item from storage (simulates DB)
          val item: Item = Storage.getItemById(itemId)
          order.addOne(itemId, OrderItem(amount, item))
        }
        callbackRef ! TestResponseMsg(
          s"Successfully added item with ID: $itemId to shopping cart of user with ID: $userId")
        contentFunction(routerRef, order) // return order with added item
      case GetOrderForUserMsg(user, checkoutRef, callbackRef) =>
        // 2) kickoff order process for current user and stop his ShoppingCart
        val articles: List[OrderItem] = order.values.toList
        checkoutRef ! CheckoutMsg(Payment(user, articles), callbackRef)
        routerRef ! CleanDirectoryMsg(user.getId)
        Behaviors.stopped
    }
}
