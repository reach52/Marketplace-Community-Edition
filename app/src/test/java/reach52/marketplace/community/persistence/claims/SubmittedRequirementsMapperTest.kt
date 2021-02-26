package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.SubmittedRequirements
import reach52.marketplace.community.persistence.claims_mapper.SubmittedRequirementsMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class SubmittedRequirementsMapperTest {

    private val submittedRequirementsModelFixture = SubmittedRequirements(
            code = "code",
            type = "type",
            dateSubmitted = ZonedDateTime.parse("2020-02-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            attachmentName = "attachmentName"
    )

    private val submittedRequirementsMapFixture = mapOf(
            SubmittedRequirementsMapper.KEY_REQUIREMENTS_CODE to "code",
            SubmittedRequirementsMapper.KEY_REQUIREMENTS_TYPE to "type",
            SubmittedRequirementsMapper.KEY_REQUIREMENTS_DATE_SUBMITTED to "2020-02-10T00:00:00Z",
            SubmittedRequirementsMapper.KEY_REQUIREMENTS_ATTACHMENT_NAME to "attachmentName"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(submittedRequirementsModelFixture,  SubmittedRequirementsMapper().unmarshal(submittedRequirementsMapFixture))
    }

    @Test
    fun marshal() {
        Assert.assertEquals(submittedRequirementsMapFixture,  SubmittedRequirementsMapper().marshal(submittedRequirementsModelFixture))
    }
}