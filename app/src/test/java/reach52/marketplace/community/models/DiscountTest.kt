package reach52.marketplace.community.models

import org.junit.Assert.assertEquals
import org.junit.Test

class DiscountTest {
    private val fixture = Discount("Test", 0.20, 0.12)

    @Test
    fun vatExempts() {
        assertEquals(89.29.toBits(), fixture.vatExempt(100.00).toBits())
    }

    @Test
    fun discountAmount() {
        assertEquals(17.86.toBits(), fixture.discountAmount(100.00).toBits())
    }

    @Test
    fun total() {
        assertEquals(71.43.toBits(), fixture.total(100.00).toBits())
    }
}