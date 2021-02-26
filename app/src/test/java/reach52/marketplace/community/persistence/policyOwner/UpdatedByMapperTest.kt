package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.policy_owner.UpdatedBy
import reach52.marketplace.community.persistence.policyOwner_mapper.UpdatedByMapper
import org.junit.Assert
import org.junit.Test

class UpdatedByMapperTest {

    private val mapper = UpdatedByMapper()
    private val updatedByFixture = UpdatedBy(
            reference = "reference",
            display = "display"
    )

    private val mapFixture = mapOf(
            UpdatedByMapper.KEY_REFERENCE to "reference",
            UpdatedByMapper.KEY_DISPLAY to "display"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(updatedByFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(updatedByFixture, mapper.unmarshal(mapFixture ))
    }
}