package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.policy_owner.Insured
import reach52.marketplace.community.persistence.policyOwner_mapper.InsuredMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class InsuredMapperTest {

    private val mapper = InsuredMapper()
    private val insuredFixture = Insured(
            reference = "reference",
            display = "display",
            address = "Pototan Iloilo",
            email = "test@email.com",
            contact = "0981213981",
            civilStatus = "single",
            gender = "male",
            dateOfBirth = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            relationship = "Father"
    )

    private val mapFixture = mapOf(
            InsuredMapper.KEY_REFERENCE_INSURED to "reference",
            InsuredMapper.KEY_DISPLAY to "display",
            InsuredMapper.KEY_ADDRESS to "Pototan Iloilo",
            InsuredMapper.KEY_EMAIL to "test@email.com",
            InsuredMapper.KEY_CONTACT to "0981213981",
            InsuredMapper.KEY_CIVIL_STATUS to "single",
            InsuredMapper.KEY_GENDER to "male",
            InsuredMapper.KEY_BIRTH_DATE to "1970-01-01T00:00:00Z",
            InsuredMapper.KEY_RELATIONSHIP to "Father"
    )


    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(insuredFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(insuredFixture, mapper.unmarshal(mapFixture))
    }
}