package reach52.marketplace.community.persistence.insurance

import reach52.marketplace.community.models.SpecificCost
import reach52.marketplace.community.persistence.SpecificCostInsuranceMapper
import org.junit.Assert
import org.junit.Test

class SpecificCostMapperTest {

    private val mapper = SpecificCostInsuranceMapper()

    private val specificCostFixture = SpecificCost(
            identifier = "id123",
            category = "sample cat",
            cost = 100.00
    )

    private val mapFixture = mapOf(
            SpecificCostInsuranceMapper.KEY_IDENTIFIER to "id123",
            SpecificCostInsuranceMapper.KEY_CATEGORY to "sample cat",
            SpecificCostInsuranceMapper.KEY_COST to 100.00
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(specificCostFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(specificCostFixture, mapper.unmarshal(mapFixture))
    }
}