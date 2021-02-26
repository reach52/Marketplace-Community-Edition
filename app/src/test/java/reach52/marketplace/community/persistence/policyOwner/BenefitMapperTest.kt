package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.policy_owner.Benefit
import reach52.marketplace.community.persistence.policyOwner_mapper.BenefitMapper
import org.junit.Assert
import org.junit.Test

class BenefitMapperTest {

    private val benefitMapper = BenefitMapper()
    private val benefitFixture = Benefit(
            category = "Hospitalization without major surgery (Public health facilities)",
            amount = 0.0,
            identifier = "HwOmS_PHF"
    )

    private val benefitMapFixture = mapOf(
            BenefitMapper.KEY_CATEGORY to "Hospitalization without major surgery (Public health facilities)",
            BenefitMapper.KEY_AMOUNT to 0.0,
            BenefitMapper.KEY_IDENTIFIER to "HwOmS_PHF"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(benefitMapFixture, benefitMapper.marshal(benefitFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(benefitFixture, benefitMapper.unmarshal(benefitMapFixture))
    }
}