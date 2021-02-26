package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.policy_owner.Beneficiaries
import reach52.marketplace.community.persistence.policyOwner_mapper.BeneficiariesMapper
import org.junit.Assert
import org.junit.Test

class BeneficiariesMapperTest {

    private val mapper = BeneficiariesMapper()
    private val beneficiariesFixture = Beneficiaries(
            reference = "reference",
            display = "display",
            relationship = "Father"
    )

    private val mapFixture = mapOf(
            BeneficiariesMapper.KEY_REFERENCE to "reference",
            BeneficiariesMapper.KEY_DISPLAY to "display",
            BeneficiariesMapper.KEY_RELATIONSHIP to "Father"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(beneficiariesFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(beneficiariesFixture, mapper.unmarshal(mapFixture))
    }
}