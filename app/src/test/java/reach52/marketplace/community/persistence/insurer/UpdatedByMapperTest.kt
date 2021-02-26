package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.UpdatedBy
import reach52.marketplace.community.persistence.insurer_mapper.UpdatedByMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdatedByMapperTest {

    private val updatedByModelFixture = UpdatedBy(
            reference = "test reference",
            display = "test@gmail.com"
    )

    private val updatedByMapFixture = mapOf(
            UpdatedByMapper.KEY_REFERENCE to "test reference",
            UpdatedByMapper.KEY_DISPLAY to "test@gmail.com"
    )

    @Test
    fun unmarshal() {
        assertEquals(updatedByModelFixture, UpdatedByMapper().unmarshal(updatedByMapFixture))
    }
}