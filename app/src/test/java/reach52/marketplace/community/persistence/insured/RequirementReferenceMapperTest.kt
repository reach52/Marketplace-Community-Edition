package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.RequirementReference
import reach52.marketplace.community.persistence.RequirementReferenceMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class RequirementReferenceMapperTest {

    private val requirementReferenceModelFixture = RequirementReference(
            completed = true,
            value = "value",
            requirementReference = "requirement reference"
    )

    private val requirementReferenceMapFixture = mapOf(
            RequirementReferenceMapper.KEY_REQ_REF_COMPLETED to true,
            RequirementReferenceMapper.KEY_REQ_REF_VALUE to "value",
            RequirementReferenceMapper.KEY_REQ_REF to "requirement reference"
    )

    @Test
    fun marshal() {
        assertEquals(requirementReferenceMapFixture, RequirementReferenceMapper().marshal(requirementReferenceModelFixture))
    }

    @Test
    fun unmarshal() {
        assertEquals(requirementReferenceModelFixture, RequirementReferenceMapper().unmarshal(requirementReferenceMapFixture))
    }
}