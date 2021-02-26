package reach52.marketplace.community.persistence.follow_up

import reach52.marketplace.community.models.follow_up.CreatedBy
import reach52.marketplace.community.persistence.followup_mapper.CreatedByMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class CreatedByTest {

    private val createdByModelFixture = CreatedBy(
            userDisplayName = "Juan Dela Cruz",
            userId = "juan@reach52.com"
    )

    private val createdByMapFixture = mapOf(
            CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz",
            CreatedByMapper.KEY_USER_ID to "juan@reach52.com"
    )

    @Test
    fun marshal() {
        assertEquals(createdByMapFixture, CreatedByMapper().marshal(createdByModelFixture))
    }

    @Test
    fun unmarshal() {
        assertEquals(createdByModelFixture, CreatedByMapper().unmarshal(createdByMapFixture))
    }
}