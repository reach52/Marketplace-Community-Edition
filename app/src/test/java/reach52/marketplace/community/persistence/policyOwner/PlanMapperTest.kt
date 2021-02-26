package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.models.policy_owner.Plan
import reach52.marketplace.community.persistence.policyOwner_mapper.BenefitMapper
import reach52.marketplace.community.persistence.policyOwner_mapper.PlanMapper
import org.junit.Assert
import org.junit.Test

class PlanMapperTest {

    private val planMapper = PlanMapper()
    private val planFixture = Plan(
            identifier = "7Y3OhYkpCx",
            tier = "Health & Personal Accident Plan",
            rate = 98568.00,
            category = "adult",
            benefits = listOf(Benefit(
                    category = "Hospitalization without major surgery (Public health facilities)",
                    amount = 0.0,
                    identifier = "HwOmS_PHF"
            ))
    )

    private val planMap = mapOf(
            PlanMapper.KEY_IDENTIFIER to "7Y3OhYkpCx",
            PlanMapper.KEY_TIER to "Health & Personal Accident Plan",
            PlanMapper.KEY_RATE to 98568.00,
            PlanMapper.KEY_CATEGORY to "adult",
            PlanMapper.KEY_BENEFITS to listOf(mapOf(
                    BenefitMapper.KEY_CATEGORY to "Hospitalization without major surgery (Public health facilities)",
                    BenefitMapper.KEY_AMOUNT to 0.0,
                    BenefitMapper.KEY_IDENTIFIER to "HwOmS_PHF"
            ))
    )

    @Test
    fun marshal() {
        Assert.assertEquals(planMap, planMapper.marshal(planFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(planFixture, planMapper.unmarshal(planMap))
    }
}