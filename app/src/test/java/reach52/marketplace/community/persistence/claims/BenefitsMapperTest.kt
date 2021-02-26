package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.Benefits
import reach52.marketplace.community.persistence.claims_mapper.BenefitsMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class BenefitsMapperTest {
    private val benefitsModelFixture = Benefits(
            claimAmount = 60.000,
            notifiedDate = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    )

    private val benefitsMapFixture = mapOf(
            BenefitsMapper.KEY_BENEFITS_CLAIM_AMOUNT to 60.00,
            BenefitsMapper.KEY_BENEFITS_NOTIFIED_DATE to "2020-01-01T00:00:00Z"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(benefitsModelFixture, BenefitsMapper().unmarshal(benefitsMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(benefitsMapFixture, BenefitsMapper().marshal(benefitsModelFixture))
    }
}