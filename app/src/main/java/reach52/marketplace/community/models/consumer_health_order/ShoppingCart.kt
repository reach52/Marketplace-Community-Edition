package reach52.marketplace.community.models.consumer_health_order


data class ShoppingCart(
        val id: String,
        val customerID: String,
        val catalogueID: OrderLineItems,
        val type: String
)