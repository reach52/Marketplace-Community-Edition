package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.AgeRange
import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.models.insurance.Formulary
import reach52.marketplace.community.persistence.insurer_mapper.AgeRangeMapper
import reach52.marketplace.community.persistence.insurer_mapper.BenefitsMapper
import reach52.marketplace.community.persistence.insurer_mapper.FormularyMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class FormularyMapperTest {
    private val formularyModelFixture = Formulary(
            identifier = "MALAYAN_BASIC_PLAN",
            tier = "basic",
            summary = "This is a sample description for Malayan Basic tier.",
            category = "adult",
            rate = 100.0,
            beneficiary = 1,
            benefits = listOf(
                    Benefit(
                            category = "Accidental Death",
                            amount = 10000.0,
                            identifier = "ACCIDENTAL_DEATH"
                    )
            ),
            ageRange = AgeRange(
                    min = 18,
                    max = 65
            )
    )

    private val formularyMapFixture = mapOf(
            FormularyMapper.KEY_IDENTIFIER to "MALAYAN_BASIC_PLAN",
            FormularyMapper.KEY_TIER to "basic",
            FormularyMapper.KEY_SUMMARY to "This is a sample description for Malayan Basic tier.",
            FormularyMapper.KEY_CATEGORY to "adult",
            FormularyMapper.KEY_RATE to 100.0,
            FormularyMapper.KEY_BENEFICIARY to 1,
            FormularyMapper.KEY_BENEFITS to listOf(
                    mapOf(
                            BenefitsMapper.KEY_CATEGORY to "Accidental Death",
                            BenefitsMapper.KEY_AMOUNT to 10000.0,
                            BenefitsMapper.KEY_IDENTIFIER to "ACCIDENTAL_DEATH"
                    )
            ),
            FormularyMapper.KEY_AGE_RANGE to mapOf(
                    AgeRangeMapper.KEY_MIN to 18,
                    AgeRangeMapper.KEY_MAX to 65
            )
    )

    @Test
    fun unmarshal() {
        assertEquals(formularyModelFixture, FormularyMapper().unmarshal(formularyMapFixture))
    }
}