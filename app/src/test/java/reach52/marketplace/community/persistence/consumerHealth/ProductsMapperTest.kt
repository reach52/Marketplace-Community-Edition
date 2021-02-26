package reach52.marketplace.community.persistence.consumerHealth

import reach52.marketplace.community.models.consumer_health_order.Products
import reach52.marketplace.community.persistence.consumerHealth_mapper.ProductsMapper
import org.junit.Assert
import org.junit.Test

class ProductsMapperTest {

    private val mapper = ProductsMapper()

    private val productsFixture = Products(
            productID = "id123",
            brand = "provider",
            miscInfo = "Free cup",
            unitOfMeasure = "test",
            price = 100.00,
            vatInclusive = true,
            name = "Product name"
    )
    private val mapFixture = mapOf(
            ProductsMapper.KEY_PRODUCT_ID to "id123",
            ProductsMapper.KEY_BRAND to "provider",
            ProductsMapper.KEY_MISC_INFO to "Free cup",
            ProductsMapper.KEY_UNIT_OF_MEASURE to "test",
            ProductsMapper.KEY_PRICE to 100.00,
            ProductsMapper.KEY_VAT to true,
            ProductsMapper.KEY_NAME to "Product name"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(productsFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(productsFixture, mapper.unmarshal(mapFixture))
    }
}