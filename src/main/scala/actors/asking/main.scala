package actors.asking

import actors.asking.Entities.TrustWorthiness.{BAD, GOOD, MODERATE}
import actors.asking.Entities.User
import actors.asking.OnlineShopActor.{AddItemMsg, PlaceOrderMsg, ShoppingMsg}
import akka.actor.typed.ActorSystem

@main
def main(): Unit = {

  // USER IDs are valid if greater than 0
  // ITEM IDs are valid if in [1 - 9]

  // 1) prepare test users
  val cr: User = User(1L, "Christiano", "Ronaldo", GOOD)
  val lm: User = User(2L, "Lionel", "Messi", MODERATE)
  val hk: User = User(3L, "Harry", "Kane", BAD)

  // 2) start up the OnlineShop
  val shop: ActorSystem[ShoppingMsg] = ActorSystem(OnlineShopActor(), "main-shop")

  // 3) perform some test requests
  shop ! AddItemMsg(cr, 3, 9L)
  shop ! AddItemMsg(lm, 45, 1L)
  shop ! AddItemMsg(hk, 1, 2L)
  shop ! AddItemMsg(cr, 1, 2L)
  shop ! PlaceOrderMsg(hk) // should be rejected
  shop ! AddItemMsg(cr, 1, 7L)
  shop ! AddItemMsg(lm, 10, 4L)
  shop ! AddItemMsg(cr, 1, 3L)
  shop ! PlaceOrderMsg(lm) // 2 items purchase
  shop ! AddItemMsg(cr, 1, 7L)
  shop ! AddItemMsg(cr, 1, 6L)
  shop ! PlaceOrderMsg(cr) // 5 items purchase
}
