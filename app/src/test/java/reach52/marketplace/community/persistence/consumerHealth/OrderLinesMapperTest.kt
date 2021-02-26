package reach52.marketplace.community.persistence.consumerHealth

import reach52.marketplace.community.models.consumer_health_order.OrderLines
import reach52.marketplace.community.persistence.consumerHealth_mapper.OrderLinesMapper
import org.junit.Assert
import org.junit.Test

class OrderLinesMapperTest {
    private val mapper = OrderLinesMapper()
    private val orderFixture  = OrderLines(
            productID = "id123",
            brand = "brand",
            miscInfo = "info",
            unitOfMeasure = "free",
            quantity = 1,
            price = 200.00,
            isVatExclusive = false,
            name = "Product name"
    )

    private val mapFixture  = mapOf(
            OrderLinesMapper.KEY_ORDER_LINE_ID to "id123",
            OrderLinesMapper.KEY_BRAND to "brand",
            OrderLinesMapper.KEY_MISC_INFO to "info",
            OrderLinesMapper.KEY_UNIT_OF_MEASURE to "free",
            OrderLinesMapper.KEY_QUANTITY to 1,
            OrderLinesMapper.KEY_PRICE to 200.00,
            OrderLinesMapper.KEY_IS_VAT_INCLUSIVE to false,
            OrderLinesMapper.KEY_NAME to "Product name"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(orderFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(orderFixture, mapper.unmarshal(mapFixture))
    }

}