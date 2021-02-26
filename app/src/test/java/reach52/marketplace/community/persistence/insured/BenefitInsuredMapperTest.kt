package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.BenefitInsured
import reach52.marketplace.community.models.RequirementReference
import reach52.marketplace.community.persistence.BenefitInsuredMapper
import reach52.marketplace.community.persistence.RequirementReferenceMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class BenefitInsuredMapperTest {

    private val benefitInsuredModelFixture = BenefitInsured(
            type = "type",
            planReference = "plan reference",
            requirement = listOf(
                    RequirementReference(
                            completed = true,
                            value = "value",
                            requirementReference = "requirement reference"
                    )
            )
    )

    private val benefitInsuredMapFixture = mapOf(
            BenefitInsuredMapper.KEY_BENEFIT_TYPE to "type",
            BenefitInsuredMapper.KEY_BENEFIT_PLAN_REFERENCE to "plan reference",
            BenefitInsuredMapper.KEY_BENEFIT_REQUIREMENT to listOf(
                    mapOf(
                            RequirementReferenceMapper.KEY_REQ_REF_COMPLETED to true,
                            RequirementReferenceMapper.KEY_REQ_REF_VALUE to "value",
                            RequirementReferenceMapper.KEY_REQ_REF to "requirement reference"
                    )
            )
    )

    @Test
    fun unmarshal() {
        assertEquals(benefitInsuredModelFixture, BenefitInsuredMapper().unmarshal(benefitInsuredMapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(benefitInsuredMapFixture, BenefitInsuredMapper().marshal(benefitInsuredModelFixture))
    }
}