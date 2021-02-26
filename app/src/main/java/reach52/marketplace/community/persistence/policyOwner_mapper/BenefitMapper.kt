package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Benefit
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class BenefitMapper : Marshaler<Benefit>, Unmarshaler<Benefit> {
    companion object {
        const val KEY_CATEGORY = "category"
        const val KEY_AMOUNT = "amount"
        const val KEY_IDENTIFIER = "identifier"
    }

    override fun marshal(t: Benefit): Map<String, Any> {
        return mapOf(
                KEY_CATEGORY to t.category,
                KEY_AMOUNT to t.amount,
                KEY_IDENTIFIER to t.identifier
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Benefit {
        return Benefit(
                category = properties[KEY_CATEGORY] as String,
                amount = (properties[KEY_AMOUNT] as Number).toDouble(),
                identifier = properties[KEY_IDENTIFIER] as String
        )
    }
}