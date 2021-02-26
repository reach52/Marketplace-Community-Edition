package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.InsuredContact
import reach52.marketplace.community.persistence.InsuredContactMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class InsuredContactMapperTest {

    private val contactModelFixture = InsuredContact(
            code = "+63",
            number = "987654321"
    )
    private val contactMapFixture = mapOf(
            InsuredContactMapper.KEY_CODE to "+63",
            InsuredContactMapper.KEY_NUMBER to "987654321"
    )

    @Test
    fun unmarshal() {
        assertEquals(contactModelFixture, InsuredContactMapper().unmarshal(contactMapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(contactMapFixture, InsuredContactMapper().marshal(contactModelFixture))
    }
}