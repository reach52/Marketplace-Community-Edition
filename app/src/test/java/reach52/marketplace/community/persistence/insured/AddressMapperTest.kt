package reach52.marketplace.community.persistence.insured

import arrow.core.Some
import reach52.marketplace.community.models.insured.Address
import reach52.marketplace.community.persistence.AddressMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class AddressMapperTest {

    private val insuredAddressModelFixture = Address(
            city = Some("city"),
            country = Some("Philippines"),
            district = Some("District 10"),
            line = listOf("line"),
            postalCode = Some("3017"),
            state = Some("State 03")
    )

    private val mapFixture = mapOf(
            AddressMapper.KEY_CITY to "city",
            AddressMapper.KEY_COUNTRY to "Philippines",
            AddressMapper.KEY_DISTRICT to "District 10",
            AddressMapper.KEY_LINE to listOf("line"),
            AddressMapper.KEY_POSTAL_CODE to "3017",
            AddressMapper.KEY_STATE to "State 03"
    )

    @Test
    fun unmarshal() {
        assertEquals(insuredAddressModelFixture, AddressMapper().unmarshal(mapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(mapFixture, AddressMapper().marshal(insuredAddressModelFixture))
    }
}