package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.SpecificCost
import reach52.marketplace.community.persistence.SpecificCostMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class SpecificCostMapperTest {

    private val specificCostModelFixture =
            SpecificCost(
                    identifier = "_id",
                    category = "category",
                    cost = 12.00
            )

    private val specificCostMapFixture = mapOf(
            SpecificCostMapper.KEY_IDENTIFIER to "_id",
            SpecificCostMapper.KEY_CATEGORY to "category",
            SpecificCostMapper.KEY_COST to 12.00
    )

    @Test
    fun marshal() {
        assertEquals(specificCostMapFixture, SpecificCostMapper().marshal(specificCostModelFixture))
    }

    @Test
    fun unmarshal() {
        assertEquals(specificCostModelFixture, SpecificCostMapper().unmarshal(specificCostMapFixture))
    }
}