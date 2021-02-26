package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.PastStatuses
import reach52.marketplace.community.persistence.claims_mapper.ClaimsPastStatusesMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsPastStatusesMapperTest {
    private val claimsPastStatusesModelFixture = PastStatuses(
            status = "status",
            username = "username",
            userID = "userID",
            dateOfStatus = ZonedDateTime.parse("2020-01-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    )

    private val claimsPastStatusesMapFixture = mapOf(
            ClaimsPastStatusesMapper.KEY_PAST_STATUS to "status",
            ClaimsPastStatusesMapper.KEY_PAST_STATUS_USERNAME to "username",
            ClaimsPastStatusesMapper.KEY_PAST_STATUS_USER_ID to "userID",
            ClaimsPastStatusesMapper.KEY_PAST_STATUS_DATE_OF_STATUS to "2020-01-10T00:00:00Z"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(claimsPastStatusesModelFixture, ClaimsPastStatusesMapper().unmarshal(claimsPastStatusesMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(claimsPastStatusesMapFixture, ClaimsPastStatusesMapper().marshal(claimsPastStatusesModelFixture))
    }
}