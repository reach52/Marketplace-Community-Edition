package reach52.marketplace.community.persistence.insurance

import reach52.marketplace.community.models.Requirement
import reach52.marketplace.community.persistence.RequirementsInsuranceMapper
import org.junit.Assert
import org.junit.Test

class RequirementsInsuranceMapperTest {

    private val mapper = RequirementsInsuranceMapper()

    private val requirementsFixture = Requirement(
            value = "some value",
            identifier = "id123"
    )

    private val mapFixture = mapOf(
            RequirementsInsuranceMapper.KEY_IDENTIFIER to "id123",
            RequirementsInsuranceMapper.KEY_VALUE to "some value"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(requirementsFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(requirementsFixture, mapper.unmarshal(mapFixture))
    }
}