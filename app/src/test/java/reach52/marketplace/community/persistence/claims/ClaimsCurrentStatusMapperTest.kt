package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.CurrentStatus
import reach52.marketplace.community.persistence.claims_mapper.ClaimsCurrentStatusMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsCurrentStatusMapperTest {

    private val claimsCurrentStatusModelFixture = CurrentStatus(
            status = "status",
            username = "username",
            userID = "userID",
            dateOfStatus = ZonedDateTime.parse("2020-02-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    )

    private val claimsCurrentStatusMapFixture = mapOf(
            ClaimsCurrentStatusMapper.KEY_STATUS to "status",
            ClaimsCurrentStatusMapper.KEY_CURRENT_STATUS_USERNAME to "username",
            ClaimsCurrentStatusMapper.KEY_CURRENT_STATUS_USER_ID to "userID",
            ClaimsCurrentStatusMapper.KEY_CURRENT_DATE_OF_STATUS to "2020-02-05T00:00:00Z"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(claimsCurrentStatusModelFixture, ClaimsCurrentStatusMapper().unmarshal(claimsCurrentStatusMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(claimsCurrentStatusMapFixture, ClaimsCurrentStatusMapper().marshal(claimsCurrentStatusModelFixture))
    }
}