package reach52.marketplace.community.persistence.insurance

import reach52.marketplace.community.models.BenefitInsurance
import reach52.marketplace.community.models.CoverageInsurance
import reach52.marketplace.community.models.Requirement
import reach52.marketplace.community.persistence.BenefitsInsuranceMapper
import reach52.marketplace.community.persistence.CoverageInsuranceMapper
import reach52.marketplace.community.persistence.RequirementsInsuranceMapper
import org.junit.Assert
import org.junit.Test

class CoverageInsuranceMapperTest {

    private val mapper = CoverageInsuranceMapper()

    private val coverageInsuranceFixture = CoverageInsurance(

            benefit = listOf(BenefitInsurance(
                    planReference = "some plan",
                    type = "typeA",
                    requirement = listOf(Requirement(
                            value = "some value",
                            identifier = "id123"
                    ))
            ))
    )

    private val mapFixture = mapOf(
            CoverageInsuranceMapper.KEY_BENEFITS to listOf(
                    mapOf(
                            BenefitsInsuranceMapper.KEY_TYPE to "typeA",
                            BenefitsInsuranceMapper.KEY_PLAN_REFERENCE to "some plan",
                            BenefitsInsuranceMapper.KEY_REQUIREMENT to listOf(
                                    mapOf(
                                            RequirementsInsuranceMapper.KEY_IDENTIFIER to "id123",
                                            RequirementsInsuranceMapper.KEY_VALUE to "some value"
                                    )
                            )
                    )
            )
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(coverageInsuranceFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(coverageInsuranceFixture, mapper.unmarshal(mapFixture))
    }
}