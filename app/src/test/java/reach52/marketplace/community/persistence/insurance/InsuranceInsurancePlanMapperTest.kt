package reach52.marketplace.community.persistence.insurance

import reach52.marketplace.community.models.InsurancePlan
import reach52.marketplace.community.models.SpecificCost
import reach52.marketplace.community.persistence.InsurancePlanMapper
import reach52.marketplace.community.persistence.SpecificCostInsuranceMapper
import org.junit.Assert
import org.junit.Test

class InsuranceInsurancePlanMapperTest {

    private val mapper = InsurancePlanMapper()

    private val planFixture = InsurancePlan(
            identifier = "id123",
            title = "Medication",
            code = "code123",
            price = 100.00,
            dateExpiry = 31536000000,
            details = "Some details",
            specificCosts = listOf(SpecificCost(
                    identifier = "id123",
                    category = "sample cat",
                    cost = 100.00
            ))
    )

    private val mapFixture = mapOf(
            InsurancePlanMapper.KEY_IDENTIFIER to "id123",
            InsurancePlanMapper.KEY_TITLE to "Medication",
            InsurancePlanMapper.KEY_CODE to "code123",
            InsurancePlanMapper.KEY_PRICE to 100.00,
            InsurancePlanMapper.KEY_DATE_EXPIRY to 31536000000,
            InsurancePlanMapper.KEY_DETAILS to "Some details",
            InsurancePlanMapper.KEY_SPECIFIC_COST to listOf(
                    mapOf(
                            SpecificCostInsuranceMapper.KEY_IDENTIFIER to "id123",
                            SpecificCostInsuranceMapper.KEY_CATEGORY to "sample cat",
                            SpecificCostInsuranceMapper.KEY_COST to 100.00
                    )
            )
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(planFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(planFixture, mapper.unmarshal(mapFixture))
    }
}