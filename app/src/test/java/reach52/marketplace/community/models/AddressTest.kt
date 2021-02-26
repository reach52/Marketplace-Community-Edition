package reach52.marketplace.community.models

import arrow.core.Some
import reach52.marketplace.community.models.insured.Address
import org.junit.Assert.assertEquals
import org.junit.Test

class AddressTest {
    private val fixture = Address(
            city = Some("City"),
            country = Some("Country"),
            district = Some("District"),
            line = listOf("Line 1", "Line 2"),
            postalCode = Some("Postal Code"),
            state = Some("State"))

    @Test
    fun text() {
        val expected = "Line 1 Line 2, District, City, State, Country Postal Code"
        assertEquals(expected, fixture.text)
    }

}