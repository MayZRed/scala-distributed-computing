package actors.asking

import actors.asking.Entities.*
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object ShoppingCartActor {

  sealed trait ShoppingCartMsg
  case class AddItemForUserMsg(userId: Long, amount: Int, item: Item) extends ShoppingCartMsg
  case class GetOrderForUserMsg(userId: Long) extends ShoppingCartMsg

  class OrderForUserResponse(userId: Long, order: List[OrderItem])

  // adapt public method to ensure the ShoppingCart is initialized empty
  def apply(): Behavior[ShoppingCartMsg] = internalFunction(Map.empty)

  private def internalFunction(orders: Map[Long, ArrayBuffer[OrderItem]]): Behavior[ShoppingCartMsg] =
    Behaviors.receiveMessagePartial {
      case AddItemForUserMsg(userId, amount, item) =>
        // TODO: add functionality
        Behaviors.same
      case GetOrderForUserMsg(userId) =>
        // TODO: add functionality
        Behaviors.same
  }

}
