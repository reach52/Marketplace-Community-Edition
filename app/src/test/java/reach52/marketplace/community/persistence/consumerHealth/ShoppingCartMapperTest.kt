package reach52.marketplace.community.persistence.consumerHealth

import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.models.consumer_health_order.OrderLines
import reach52.marketplace.community.models.consumer_health_order.ShoppingCart
import reach52.marketplace.community.persistence.consumerHealth_mapper.OrderLinesMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.ShoppingCartMapper
import org.junit.Assert
import org.junit.Test

class ShoppingCartMapperTest {

    private val mapper = ShoppingCartMapper()
    private val shoppingFixture = ShoppingCart(
            customerID = "residentId",
            catalogueID = OrderLineItems(
                    OrderLines(
                            productID = "id123",
                            brand = "brand",
                            miscInfo = "info",
                            unitOfMeasure = "free",
                            quantity = 1,
                            price = 200.00,
                            isVatExclusive = false,
                            name = "Product name"
                    ), listOf()
            ),
            id = "id123",
            type = "shoppingCart"
    )

    private val mapFixture = mapOf(
            ShoppingCartMapper.KEY_CUSTOMER_ID to "residentId",
            ShoppingCartMapper.KEY_CATALOGUE_ID to listOf(
                    mapOf(
                            OrderLinesMapper.KEY_ORDER_LINE_ID to "id123",
                            OrderLinesMapper.KEY_BRAND to "brand",
                            OrderLinesMapper.KEY_MISC_INFO to "info",
                            OrderLinesMapper.KEY_UNIT_OF_MEASURE to "free",
                            OrderLinesMapper.KEY_QUANTITY to 1,
                            OrderLinesMapper.KEY_PRICE to 200.00,
                            OrderLinesMapper.KEY_IS_VAT_INCLUSIVE to false,
                            OrderLinesMapper.KEY_NAME to "Product name"
                    )
            ),
            ShoppingCartMapper.KEY_SHOPPING_ID to "id123",
            ShoppingCartMapper.KEY_TYPE to "shoppingCart"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(shoppingFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(shoppingFixture, mapper.unmarshal(mapFixture))
    }
}