package reach52.marketplace.community.persistence.consumerHealth

import reach52.marketplace.community.models.consumer_health_order.CreatedBy
import reach52.marketplace.community.persistence.consumerHealth_mapper.CreatedByMapper
import org.junit.Assert
import org.junit.Test

class CreatedByMapperTest {

    private val mapper = CreatedByMapper()
    private val createdByFixture = CreatedBy(
            userId = "juan@email.com",
            userDisplayName = "Juan Dela Cruz"
    )

    private val mapFixture = mapOf(
            CreatedByMapper.KEY_USER_ID to "juan@email.com",
            CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(createdByFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(createdByFixture, mapper.unmarshal(mapFixture))
    }
}