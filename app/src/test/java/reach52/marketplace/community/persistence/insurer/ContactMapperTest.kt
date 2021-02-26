package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Address
import reach52.marketplace.community.models.insurance.Contact
import reach52.marketplace.community.persistence.insurer_mapper.AddressMapper
import reach52.marketplace.community.persistence.insurer_mapper.ContactMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactMapperTest {

    private val contactModelFixture = Contact(
            phone = "+639123456780",
            email = "mailto@malayan.com",
            address = listOf(
                    Address(
                            line = listOf("line","some_line"),
                            city = "Manila",
                            country = "Philippines",
                            state = "State 03",
                            zip = "1800"
                    )
            )
    )

    private val contactMapFixture = mapOf(
            ContactMapper.KEY_PHONE to "+639123456780",
            ContactMapper.KEY_EMAIL to "mailto@malayan.com",
            ContactMapper.KEY_ADDRESS to listOf(
                    mapOf(
                            AddressMapper.KEY_LINE to listOf("line","some_line"),
                            AddressMapper.KEY_CITY to "Manila",
                            AddressMapper.KEY_COUNTRY to "Philippines",
                            AddressMapper.KEY_STATE to "State 03",
                            AddressMapper.KEY_ZIP to "1800"
                    )
            )
    )

    @Test
    fun unmarshal() {
        assertEquals(contactModelFixture, ContactMapper().unmarshal(contactMapFixture))
    }
}