package reach52.marketplace.community.persistence.consumerHealth_mapper

import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.models.consumer_health_order.ShoppingCart
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class ShoppingCartMapper:Marshaler<ShoppingCart>, Unmarshaler<ShoppingCart> {
    companion object{
        const val KEY_SHOPPING_ID = "_id"
        const val KEY_CUSTOMER_ID = "customerID"
        const val KEY_CATALOGUE_ID = "catalogueID"
        const val KEY_TYPE = "type"
        const val TYPE_SHOPPING_CART = "shoppingCart"

        private val orderLinesMapper = OrderLinesMapper()
    }

    override fun marshal(t: ShoppingCart): Map<String, Any> {
        return mapOf(
                KEY_CUSTOMER_ID to t.customerID,
                KEY_CATALOGUE_ID to t.catalogueID.list.map(orderLinesMapper::marshal)  ,
                KEY_SHOPPING_ID to t.id,
                KEY_TYPE to t.type
        )
    }

    override fun unmarshal(properties: Map<String, Any>): ShoppingCart {
        val id = properties[KEY_SHOPPING_ID] as String
        @Suppress("UNCHECKED_CAST")
        val customerID = properties[KEY_CUSTOMER_ID] as String
        val catalogueID = (properties[KEY_CATALOGUE_ID] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(orderLinesMapper::unmarshal)
                .let { OrderLineItems.fromList(it) }
        val type = properties[KEY_TYPE] as String
        return ShoppingCart(
                id = id,
                customerID = customerID,
                catalogueID = catalogueID,
                type = type
        )
    }
}