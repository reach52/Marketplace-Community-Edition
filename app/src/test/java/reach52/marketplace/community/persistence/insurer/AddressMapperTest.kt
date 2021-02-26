package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Address
import reach52.marketplace.community.persistence.insurer_mapper.AddressMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class AddressMapperTest {

    private val addressModelFixture = Address(
            line = listOf("line","some_line"),
            city = "Manila",
            country = "Philippines",
            state = "State 03",
            zip = "1800"
    )

    private val mapFixture = mapOf(
            AddressMapper.KEY_LINE to listOf("line","some_line"),
            AddressMapper.KEY_CITY to "Manila",
            AddressMapper.KEY_COUNTRY to "Philippines",
            AddressMapper.KEY_STATE to "State 03",
            AddressMapper.KEY_ZIP to "1800"
    )

    @Test
    fun unmarshal() {
        assertEquals(addressModelFixture, AddressMapper().unmarshal(mapFixture))
    }

}