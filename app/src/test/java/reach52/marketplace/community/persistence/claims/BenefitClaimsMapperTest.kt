package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.BenefitClaims
import reach52.marketplace.community.persistence.claims_mapper.BenefitClaimsMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class BenefitClaimsMapperTest {

    private val benefitClaimsModelFixture = BenefitClaims(
            claimAmount = 60.000,
            claimNotifiedDate = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            lastClaimSubmittedDate = ZonedDateTime.parse("2020-01-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            userID = "userID"
    )

    private val benefitClaimsMapFixture = mapOf(
            BenefitClaimsMapper.KEY_CLAIM_AMOUNT to 60.00,
            BenefitClaimsMapper.KEY_CLAIM_NOTIFIED_DATE to "2020-01-01T00:00:00Z",
            BenefitClaimsMapper.KEY_LAST_CLAIM_SUBMITTED_DATE to "2020-01-10T00:00:00Z",
            BenefitClaimsMapper.KEY_USER_ID to "userID"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(benefitClaimsModelFixture, BenefitClaimsMapper().unmarshal(benefitClaimsMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(benefitClaimsMapFixture, BenefitClaimsMapper().marshal(benefitClaimsModelFixture))
    }
}