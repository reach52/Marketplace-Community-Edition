package reach52.marketplace.community.persistence.insurer

import reach52.marketplace.community.models.insurance.Policy
import reach52.marketplace.community.persistence.insurer_mapper.PolicyMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class PolicyMapperTest {
    private val policyModelFixture = Policy(
            text = "<h2>This is a sample insurance policy for Malayan. /n <h2>",
            url = "https://malayan.com.ph/insurance-policy"
    )

    private val policyMapFixture = mapOf(
            PolicyMapper.KEY_TEXT to "<h2>This is a sample insurance policy for Malayan. /n <h2>",
            PolicyMapper.KEY_URL to "https://malayan.com.ph/insurance-policy"
    )

    @Test
    fun unmarshal() {
        assertEquals(policyModelFixture, PolicyMapper().unmarshal(policyMapFixture))
    }
}