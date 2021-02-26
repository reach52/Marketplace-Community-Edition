package reach52.marketplace.community.persistence.medication

import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.persistence.DiscountMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class DiscountMapperTest {
    private val mapper = DiscountMapper()

    private val fixture = Discount(code = "test", discount = 0.20, vat = 0.12)

    private val map = mapOf(
            DiscountMapper.KEY_CODE to "test",
            DiscountMapper.KEY_DISCOUNT to 0.20,
            DiscountMapper.KEY_VAT to 0.12
    )

    @Test
    fun marshal() {
        assertEquals(map, mapper.marshal(fixture))
    }

    @Test
    fun unmarshalTest() {
        assertEquals(fixture, mapper.unmarshal(map))
    }
}