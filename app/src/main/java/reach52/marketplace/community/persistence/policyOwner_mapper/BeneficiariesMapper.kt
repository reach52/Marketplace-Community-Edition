package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Beneficiaries
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class BeneficiariesMapper: Marshaler<Beneficiaries>, Unmarshaler<Beneficiaries> {

    companion object{
        const val KEY_REFERENCE = "reference"
        const val KEY_DISPLAY = "display"
        const val KEY_RELATIONSHIP = "relationship"
    }

    override fun marshal(t: Beneficiaries): Map<String, Any> {
        return mapOf(
                KEY_REFERENCE to t.reference,
                KEY_DISPLAY to t.display,
                KEY_RELATIONSHIP to t.relationship
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Beneficiaries {
        val reference = properties[KEY_REFERENCE] as String
        val display = properties[KEY_DISPLAY] as String
        val relationship = properties[KEY_RELATIONSHIP] as String

        return Beneficiaries(
                reference = reference,
                display = display,
                relationship = relationship
        )
    }
}