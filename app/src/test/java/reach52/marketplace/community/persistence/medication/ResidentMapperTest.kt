package reach52.marketplace.community.persistence.medication

import arrow.core.None
import arrow.core.Some
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.insured.Address
import reach52.marketplace.community.persistence.ResidentMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate

open class ResidentMapperTest {
    private val mapper = ResidentMapper()

    private val mapFixture = mapOf(
            ResidentMapper.KEY_ID to "ID",
            ResidentMapper.KEY_ANSWERS to mapOf(
                    ResidentMapper.KEY_ADDRESS_1 to "Address 1",
                    ResidentMapper.KEY_ADDRESS_2 to "Address 2",
                    ResidentMapper.KEY_BIRTH_DATE to "1970-01-01T00:00:00.000Z",
                    ResidentMapper.KEY_COUNTRY to "Country",
                    ResidentMapper.KEY_FIRST_NAME to "First",
                    ResidentMapper.KEY_GENDER to "F",
                    ResidentMapper.KEY_LAST_NAME to "Last",
                    ResidentMapper.KEY_MARITAL_STATUS to "Marital Status",
                    ResidentMapper.KEY_MIDDLE_NAME to "Middle",
                    ResidentMapper.KEY_POSTAL_CODE to "Postal Code",
                    ResidentMapper.KEY_PROVINCE_CITY to "Province",
                    ResidentMapper.KEY_CONTACT to "09876543121",
                    ResidentMapper.KEY_EMAIL to "email@email.com"
            )
    )

    private val residentFixture = Resident(
            address =
            Address(city = Some("Province"),
                    country = Some("Country"),
                    district = None,
                    line = listOf("Address 1", "Address 2"),
                    postalCode = Some("Postal Code"),
                    state = None),
            birthDate = LocalDate.of(1970, 1, 1),
            firstName = Some("First"),
            gender = "F",
            id = "ID",
            lastName = Some("Last"),
            maritalStatus = Some("Marital Status"),
            middleName = Some("Middle"),
            contact = Some("09876543121"),
            email = Some("email@email.com")
    )

    @Test
    fun unmarshal() {
        assertEquals(residentFixture, mapper.unmarshal(mapFixture))
    }
}