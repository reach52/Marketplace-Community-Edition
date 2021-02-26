package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.persistence.insurer_mapper.BenefitsMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class BenefitsMapperTest {
    private val benefitsModelFixture = Benefit(
            category = "Accidental Death",
            amount = 10000.0,
            identifier = "ACCIDENTAL_DEATH"
    )

    private val benefitsMapFixture = mapOf(
            BenefitsMapper.KEY_CATEGORY to "Accidental Death",
            BenefitsMapper.KEY_AMOUNT to 10000.0,
            BenefitsMapper.KEY_IDENTIFIER to "ACCIDENTAL_DEATH"
    )

    @Test
    fun unmarshal() {
        assertEquals(benefitsModelFixture, BenefitsMapper().unmarshal(benefitsMapFixture))
    }

}