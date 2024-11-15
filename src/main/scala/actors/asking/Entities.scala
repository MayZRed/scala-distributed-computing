package actors.asking

object Entities {

  // this object contains all business entities needed for the web-shop example

  enum TrustWorthiness(val limit: Double):
    case GOOD extends TrustWorthiness(1000.00)
    case MODERATE extends TrustWorthiness(100.00)
    case BAD extends TrustWorthiness(10.00)
  end TrustWorthiness

  enum PaymentType:
    case PAYPAL, CREDIT, DEBIT

  class User(id: Long, name: String, sirName: String, trust: TrustWorthiness)

  class Item(id: Long, designation: String, price: Double)

  class OrderItem(amount: Int, item: Item)

  class Payment(customer: User, pType: PaymentType, order: List[OrderItem])

  class Invoice(customer: User, itemList: List[String], priceSum: Double)

}