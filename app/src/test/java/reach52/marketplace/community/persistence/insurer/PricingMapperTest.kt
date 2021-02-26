package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Pricing
import reach52.marketplace.community.persistence.insurer_mapper.PricingMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class PricingMapperTest {
    private val pricingModelFixture = Pricing(
            text = "Philippine peso",
            unit = "Php."
    )

    private val pricingMapFixture = mapOf(
            PricingMapper.KEY_TEXT to "Philippine peso",
            PricingMapper.KEY_UNIT to "Php."
    )

    @Test
    fun unmarshal() {
        assertEquals(pricingModelFixture, PricingMapper().unmarshal(pricingMapFixture))
    }

}