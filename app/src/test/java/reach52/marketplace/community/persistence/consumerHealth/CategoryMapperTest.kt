package reach52.marketplace.community.persistence.consumerHealth

import reach52.marketplace.community.models.consumer_health_order.Category
import reach52.marketplace.community.persistence.consumerHealth_mapper.CategoryMapper
import org.junit.Assert
import org.junit.Test

class CategoryMapperTest {

    private val mapper = CategoryMapper()
    private val categoryFixture = Category(
            categoryID = "id123",
            category = "Home Test Kit"
    )

    private val mapFixture = mapOf(
            CategoryMapper.KEY_CATEGORY_ID to "id123",
            CategoryMapper.KEY_CATEGORY to "Home Test Kit"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(categoryFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(categoryFixture, mapper.unmarshal(mapFixture))
    }
}