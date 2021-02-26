package reach52.marketplace.community.models

import arrow.core.Some
import reach52.marketplace.community.models.insured.Address
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

class ResidentTest {
    private val fixture = Resident(
            address = Address(
                    city = Some("City"),
                    country = Some("Country"),
                    district = Some("District"),
                    line = listOf("Line 1", "Line 2"),
                    postalCode = Some("Postal Code"),
                    state = Some("State")
            ),
            birthDate = LocalDate.of(1970, 1, 1),
            firstName = Some("First"),
            gender = "F",
            id = "ID",
            lastName = Some("Last"),
            maritalStatus = Some("Marital Status"),
            middleName = Some("Middle"),
            contact = Some("09876543211"),
            email = Some("email@email.com")
    )

    @Test
    fun age() {
        assertEquals("Last, First Middle", fixture.name)
    }

    @Test
    fun name() {
        val expected = Period.between(fixture.birthDate, LocalDate.now()).years
        assertEquals(expected, fixture.age)
    }
}