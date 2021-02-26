package reach52.marketplace.community.persistence.consumerHealth

import reach52.marketplace.community.models.consumer_health_order.CreatedBy
import reach52.marketplace.community.models.consumer_health_order.Products
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import reach52.marketplace.community.persistence.consumerHealth_mapper.CreatedByMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.ProductsMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.ServiceOrProductMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ServiceOrProductMapperTest {

    private val mapServiceProduct = ServiceOrProduct(
            id = "id123",
            dateCreated = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            category = "Home Test Kit",
            products = listOf(Products(
                    productID = "id123",
                    brand = "provider",
                    miscInfo = "Free cup",
                    unitOfMeasure = "test",
                    price = 100.00,
                    vatInclusive = true,
                    name = "Product name"
            )),
            type = "serviceProduct",
            createdBy = CreatedBy(
                    userId = "juan@email.com",
                    userDisplayName = "Juan Dela Cruz"
            ),
            updatedBy = listOf(CreatedBy(
                    userId = "juan@email.com",
                    userDisplayName = "Juan Dela Cruz"
            )),
            deletedBy = CreatedBy(
                    userId = "juan@email.com",
                    userDisplayName = "Juan Dela Cruz"
            )
    )

    private val mapFixture = mapOf(
            ServiceOrProductMapper.KEY_ID to "id123",
            ServiceOrProductMapper.KEY_DATE_CREATED to "2020-07-05T00:00:00Z",
            ServiceOrProductMapper.KEY_CATEGORY to "Home Test Kit",
            ServiceOrProductMapper.KEY_PRODUCTS to listOf(
                    mapOf(
                        ProductsMapper.KEY_PRODUCT_ID to "id123",
                        ProductsMapper.KEY_BRAND to "provider",
                        ProductsMapper.KEY_MISC_INFO to "Free cup",
                        ProductsMapper.KEY_UNIT_OF_MEASURE to "test",
                        ProductsMapper.KEY_PRICE to 100.00,
                        ProductsMapper.KEY_VAT to true,
                            ProductsMapper.KEY_NAME to "Product name"
                    )
            ),
            ServiceOrProductMapper.KEY_TYPE to "serviceProduct",
            ServiceOrProductMapper.KEY_CREATED_BY to mapOf(
                    CreatedByMapper.KEY_USER_ID to "juan@email.com",
                    CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
            ),
            ServiceOrProductMapper.KEY_UPDATED_BY to listOf(
                    mapOf(
                        CreatedByMapper.KEY_USER_ID to "juan@email.com",
                        CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
                    )
            ),
            ServiceOrProductMapper.KEY_DELETED to mapOf(
                    CreatedByMapper.KEY_USER_ID to "juan@email.com",
                    CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
            )

    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, ServiceOrProductMapper().marshal(mapServiceProduct))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(mapServiceProduct, ServiceOrProductMapper().unmarshal(mapFixture))
    }
}