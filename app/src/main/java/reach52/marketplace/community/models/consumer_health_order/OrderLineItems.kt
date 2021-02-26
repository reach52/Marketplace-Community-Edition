package reach52.marketplace.community.models.consumer_health_order

data class OrderLineItems(private val first: OrderLines, private val rest: List<OrderLines> ) {
    companion object{
        fun fromList(items: List<OrderLines>): OrderLineItems = OrderLineItems(items.first(), items.drop(1))
    }
    val list : List<OrderLines> = mutableListOf(first).also { it += rest }
}