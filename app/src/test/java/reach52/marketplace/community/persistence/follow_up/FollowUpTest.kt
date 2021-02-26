package reach52.marketplace.community.persistence.follow_up

import reach52.marketplace.community.models.follow_up.CreatedBy
import reach52.marketplace.community.models.follow_up.FollowUp
import reach52.marketplace.community.persistence.followup_mapper.CreatedByMapper
import reach52.marketplace.community.persistence.followup_mapper.FollowUpMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class FollowUpTest {
    private val followUpModelFixture = FollowUp(
            productId = "xd561y79-b180-9571-98a3-bad732310x3y",
            productName = "insurance",
            residentId = "022675f9-bb90-4690-8ed8-ba6d323008ed",
            residentName = "Sebastian, Mary Jane Nicolas",
            email = "test@gmail.com",
            contact = "0987654121",
            status = "active",
            fbId = "zzx675f9-bb90-oa14-8ed8-ba6d32xa2pa1",
            reason = "Did not have money",
            notes = "the quick brown fox jumps over the lazy dog",
            address = "Sample City",
            productDescription = "insurance gold plan",
            dateFollowUp = ZonedDateTime.parse("2019-12-20T01:56:10.459Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            dateCreated = ZonedDateTime.parse("2020-02-20T05:10:13.877Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            createdBy = CreatedBy(
                    userId = "juan@reach52.com",
                    userDisplayName = "Juan Dela Cruz"
            ),
            type = "followup"
    )

    private val followUpMapFixture = mapOf(
            FollowUpMapper.KEY_PRODUCT_ID to "xd561y79-b180-9571-98a3-bad732310x3y",
            FollowUpMapper.KEY_PRODUCT_NAME to "insurance",
            FollowUpMapper.KEY_RESIDENT_ID to "022675f9-bb90-4690-8ed8-ba6d323008ed",
            FollowUpMapper.KEY_RESIDENT_NAME to "Sebastian, Mary Jane Nicolas",
            FollowUpMapper.KEY_EMAIL to "test@gmail.com",
            FollowUpMapper.KEY_CONTACT to "0987654121",
            FollowUpMapper.KEY_STATUS to "active",
            FollowUpMapper.KEY_FB_ID to "zzx675f9-bb90-oa14-8ed8-ba6d32xa2pa1",
            FollowUpMapper.KEY_REASON to "Did not have money",
            FollowUpMapper.KEY_NOTES to "the quick brown fox jumps over the lazy dog",
            FollowUpMapper.KEY_ADDRESS to "Sample City",
            FollowUpMapper.KEY_PRODUCT_DESCRIPTION to "insurance gold plan",
            FollowUpMapper.KEY_DATE_FOLLOW_UP to "2019-12-20T01:56:10.459Z",
            FollowUpMapper.KEY_DATE_CREATED to "2020-02-20T05:10:13.877Z",
            FollowUpMapper.KEY_CREATED_BY to mapOf(
                    CreatedByMapper.KEY_USER_ID to "juan@reach52.com",
                    CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
            ),
            FollowUpMapper.KEY_TYPE to "followup"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(followUpMapFixture, FollowUpMapper().marshal(followUpModelFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(followUpModelFixture, FollowUpMapper().unmarshal(followUpMapFixture))
    }
}