package reach52.marketplace.community.persistence.medication

import reach52.marketplace.community.models.medication.Physician
import reach52.marketplace.community.persistence.medication_mapper.PhysicianMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class PhysicianMapperTest {
    private val mapper = PhysicianMapper()

    private val mapFixture = mapOf(
            PhysicianMapper.KEY_ID to "ID",
            PhysicianMapper.KEY_LICENCE_NUMBER to "License Number",
            PhysicianMapper.KEY_NAME to "Name"
    )

    private val physicianFixture = Physician(
            id = "ID",
            licenseNumber = "License Number",
            name = "Name"
    )

    @Test
    fun unmarshal() {
        assertEquals(physicianFixture, mapper.unmarshal(mapFixture))
    }
}