package reach52.marketplace.community.persistence.insured

import reach52.marketplace.community.models.CoverageInsured
import reach52.marketplace.community.persistence.CoverageInsuredMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class CoverageInsuredMapperTest {

    private val coverageInsuredModelFixture = CoverageInsured(
            beneficiary = "beneficiary",
            dependent = "dependent",
            relationship = "relationship"
    )

    private val coverageInsuredMapFixture = mapOf(
            CoverageInsuredMapper.KEY_COVERAGE_BENEFICIARY to "beneficiary",
            CoverageInsuredMapper.KEY_COVERAGE_DEPENDENT to "dependent",
            CoverageInsuredMapper.KEY_COVERAGE_RELATIONSHIP to "relationship"
    )

    @Test
    fun unmarshal() {
        assertEquals(coverageInsuredModelFixture, CoverageInsuredMapper().unmarshal(coverageInsuredMapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(coverageInsuredMapFixture, CoverageInsuredMapper().marshal(coverageInsuredModelFixture))
    }
}