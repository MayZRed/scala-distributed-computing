package actors.asking

object Entities {

  // this object contains all business entities needed for the web-shop example

  enum TrustWorthiness(val limit: Double):
    case GOOD extends TrustWorthiness(3000.00)
    case MODERATE extends TrustWorthiness(300.00)
    case BAD extends TrustWorthiness(30.00)
  end TrustWorthiness

  class User(id: Long, name: String, sirName: String, trust: TrustWorthiness) {
    require(id > 0) // IDs for users must be strictly positive
    def getId: Long = id
    def getName: String = name + " " + sirName
    def getTrustLevel: TrustWorthiness = trust
  }

  class Item(id: Long, designation: String, price: Double) {
    require(id > 0) // IDs for items must be strictly positive
    require(price > 0.00) // item must have a valid price
    def getDesignation: String = designation
    def getPrice: Double = price
  }

  class OrderItem(amount: Int, item: Item) {
    def getAmount: Int = amount
    def getItem: Item = item
  }

  class Payment(customer: User, order: List[OrderItem]) {
    def getCustomerInformation: User = customer
    def getOrderDetails: List[OrderItem] = order
  }

  class Invoice(customer: User, orderDetails: List[OrderItem], priceSum: Double) {
    def printInvoice(): Unit = {
      val leftBorder: String = "| "
      val rightBorder: String = " |"
      val horizontalBorder: String = "|".padTo(55, '-') + "|"
      val newLine: String = "\n"
      def textLine(str: String): String = leftBorder + str.padTo(52, ' ') + rightBorder
      def itemLine(item: OrderItem): String = {
        val amountStr = item.getAmount.toString.padTo(5, ' ')
        val nameStr = item.getItem.getDesignation.padTo(35, ' ')
        val priceStr = padPrice(item.getItem.getPrice)
        val line = amountStr + "| " + nameStr + "| " + priceStr
        leftBorder + line + rightBorder
      }
      def padPrice(price: Double): String = price.toString.reverse.padTo(8, ' ').reverse

      println(newLine + horizontalBorder)
      println(leftBorder.padTo(54, ' ') + rightBorder)
      println(textLine(s"Dear ${customer.getName},"))
      println(textLine("Thank you for your purchasing the following items:"))
      println(leftBorder.padTo(54, ' ') + rightBorder)
      println(leftBorder.padTo(54, ' ') + rightBorder)
      println(horizontalBorder)
      orderDetails.foreach(el => println(itemLine(el)))
      println(horizontalBorder)
      println(leftBorder + "Total: ".padTo(44, ' ') + padPrice(priceSum) + rightBorder)
      println(leftBorder.padTo(54, ' ') + rightBorder)
      println(textLine("Best regards,"))
      println(textLine("your OnlineShop Team"))
      println(horizontalBorder)
    }
  }

}