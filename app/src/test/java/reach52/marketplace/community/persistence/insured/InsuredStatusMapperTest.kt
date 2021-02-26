package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.Status
import reach52.marketplace.community.persistence.InsuredStatusMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsuredStatusMapperTest {

    private val insuredStatusModelFixture = Status(
            status = "status",
            statusDate = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            username = "username",
            usernameId = "usernameID",
            userDisplayName = "userDisplayName"
    )

    private val insuredStatusMapFixture = mapOf(
            InsuredStatusMapper.KEY_STATUS to "status",
            InsuredStatusMapper.KEY_STATUS_DATE to "2020-07-05T00:00Z",
            InsuredStatusMapper.KEY_USERNAME to "username",
            InsuredStatusMapper.KEY_USERNAME_ID to "usernameID",
            InsuredStatusMapper.KEY_USER_DISPLAY_NAME to "userDisplayName"
    )


    @Test
    fun unmarshal() {
        assertEquals(insuredStatusModelFixture, InsuredStatusMapper().unmarshal(insuredStatusMapFixture))
    }


    @Test
    fun marshal() {
        assertEquals(insuredStatusMapFixture, InsuredStatusMapper().marshal(insuredStatusModelFixture))
    }
}