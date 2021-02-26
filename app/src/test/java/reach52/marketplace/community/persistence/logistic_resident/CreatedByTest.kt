package reach52.marketplace.community.persistence.logistic_resident

import reach52.marketplace.community.models.logistic_resident.CreatedBy
import reach52.marketplace.community.persistence.logistic_mapper.CreatedByMapper
import org.junit.Test
import org.junit.Assert.assertEquals
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class CreatedByTest {

    private val createdByModelFixture = CreatedBy(
            dateCreated = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            username = "juan@reach52.com",
            userId = "juan@reach52.com",
            userDisplayName = "Juan Dela Cruz"
    )

    private val createdByMapFixture = mapOf(
            CreatedByMapper.KEY_DATE_CREATED to "2020-07-05T00:00Z",
            CreatedByMapper.KEY_USERNAME to "juan@reach52.com",
            CreatedByMapper.KEY_USER_ID to "juan@reach52.com",
            CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
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