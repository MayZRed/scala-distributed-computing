package actors.asking

import actors.asking.Entities.*
import actors.asking.OnlineShopActor.{ActionFailedMsg, CheckoutInvoice, ShoppingMsg}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.math.BigDecimal

object CheckoutActor {

  class CheckoutMsg(infos: Payment, callbackTo: ActorRef[ShoppingMsg]) {
    def getInfos: Payment = infos
    def getCallbackReference: ActorRef[ShoppingMsg] = callbackTo
  }

  def apply(): Behavior[CheckoutMsg] = Behaviors.receiveMessage { msg =>
    // 1) retrieve limit and total for price
    val customer: User = msg.getInfos.getCustomerInformation
    val orderDetails: List[OrderItem] = msg.getInfos.getOrderDetails
    val priceLimit: Double = customer.getTrustLevel.limit
    val totalPrice: Double = calculateTotalPrice(orderDetails)

    // 2) check if purchase is trusted / accepted
    if (totalPrice <= priceLimit) {
      // 2.1) payment is trusted and therefore succeeds
      msg.getCallbackReference ! CheckoutInvoice(Invoice(customer, orderDetails, totalPrice))
    } else {
      // 2.2) payment is not trusted and therefore is rejected
      msg.getCallbackReference ! ActionFailedMsg(
        s"Payment of user with ID: ${customer.getId} was rejected! Pleas check your trust worthiness.")
    }
    Behaviors.same
  }

  private def calculateTotalPrice(orderDetails: List[OrderItem]): Double = {
    var totalPrice = BigDecimal(0.00)
    orderDetails.foreach(el => totalPrice += (el.getItem.getPrice * el.getAmount))
    totalPrice.setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble // return summarized prices (inc. amount)
  }

}
