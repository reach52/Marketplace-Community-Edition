package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.SubmittedBasicRequirements
import reach52.marketplace.community.persistence.claims_mapper.SubmittedBasicRequirementsMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class SubmittedBasicRequirementsMapperTest {

    private val submittedBasicRequirementsModelFixture = SubmittedBasicRequirements(
            code = "code",
            type = "type",
            dateSubmitted = ZonedDateTime.parse("2020-01-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            attachmentName = "attachmentName"
    )

    private val submittedBasicRequirementsMapFixture = mapOf(
            SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_CODE to "code",
            SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_TYPE to "type",
            SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_DATE_SUBMITTED to "2020-01-10T00:00:00Z",
            SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_ATTACHMENT_NAME to "attachmentName"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(submittedBasicRequirementsModelFixture, SubmittedBasicRequirementsMapper().unmarshal(submittedBasicRequirementsMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(submittedBasicRequirementsMapFixture, SubmittedBasicRequirementsMapper().marshal(submittedBasicRequirementsModelFixture))
    }
}