package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Plan
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import reach52.marketplace.community.persistence.insurer_mapper.BenefitsMapper

class PlanMapper : Marshaler<Plan>, Unmarshaler<Plan>{

    companion object {
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_TIER = "tier"
        const val KEY_RATE = "price"
        const val KEY_CATEGORY = "category"
        const val KEY_BENEFITS = "benefits"

        private val benefitsMapper = BenefitsMapper()
    }

    override fun marshal(t: Plan): Map<String, Any> {
        return mapOf(
                KEY_IDENTIFIER to t.identifier,
                KEY_TIER to t.tier,
                KEY_RATE to t.rate,
                KEY_CATEGORY to t.category,
                KEY_BENEFITS to t.benefits.map(benefitsMapper::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Plan {
        return Plan(
                identifier = properties[KEY_IDENTIFIER] as String,
                tier = properties[KEY_TIER] as String,
                rate = (properties[KEY_RATE] as Number).toDouble(),
                category = properties[KEY_CATEGORY] as String,
                benefits = (properties[KEY_BENEFITS] as List<*>)
                        .filterIsInstance<Map<String, Any>>()
                        .map(benefitsMapper::unmarshal)
        )
    }

}
