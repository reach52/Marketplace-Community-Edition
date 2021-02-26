package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Requirement
import reach52.marketplace.community.persistence.insurer_mapper.RequirementsMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class RequirementsMapperTest {
    private val requirementsModelFixture = Requirement(
            identifier = "GOVERNMENT_ID",
            category = "Any Government ID",
            option = true,
            variations = listOf(
                    "SSS",
                    "TIN",
                    "PHIL-HEALTH"
            ),
            references = listOf(
                    "MALAYAN_BASIC_PLAN",
                    "MALAYAN_SILVER_PLAN",
                    "MALAYAN_BRONZE_PLAN",
                    "MALAYAN_GOLD_PLAN",
                    "MALAYAN_PLATINUM_PLAN"
            )
    )

    private val requirementsMapFixture = mapOf(
            RequirementsMapper.KEY_IDENTIFIER to "GOVERNMENT_ID",
            RequirementsMapper.KEY_CATEGORY to "Any Government ID",
            RequirementsMapper.KEY_OPTION to true,
            RequirementsMapper.KEY_VARIATIONS to listOf(
                    "SSS",
                    "TIN",
                    "PHIL-HEALTH"
            ),
            RequirementsMapper.KEY_REFERENCES to listOf(
                    "MALAYAN_BASIC_PLAN",
                    "MALAYAN_SILVER_PLAN",
                    "MALAYAN_BRONZE_PLAN",
                    "MALAYAN_GOLD_PLAN",
                    "MALAYAN_PLATINUM_PLAN"
            )
    )

    @Test
    fun marshal() {
        assertEquals(requirementsModelFixture, RequirementsMapper().unmarshal(requirementsMapFixture))
    }
}