package actors.asking

import actors.asking.Entities.Item

object Storage {

  private val item_storage: Map[Long, Item] = Map(
    1L -> Item(1L, "9V A++ Battery", 1.99),
    2L -> Item(2L, "AMD Ryzen 7 9800x3D", 549.99),
    3L -> Item(3L, "GX Trust Wireless Headset", 87.49),
    4L -> Item(4L, "Lithium Chip Battery", 2.49),
    5L -> Item(5L, "JBL Flip 5", 73.99),
    6L -> Item(6L, "Nintendo Switch", 244.00),
    7L -> Item(7L, "Seasonic GX-850 Power Unit", 89.99),
    8L -> Item(8L, "Samsung Wireless Charger", 16.99),
    9L -> Item(9L, "Dell 27 Curved Gaming Monitor", 142.99)
  )

  def getItemById(itemId: Long): Item = item_storage(itemId)

}
