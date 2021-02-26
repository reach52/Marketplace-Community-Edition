package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import reach52.marketplace.community.persistence.policyOwner_mapper.BenefitMapper

class BenefitsMapper : Marshaler<Benefit>, Unmarshaler<Benefit>{
    companion object {
        const val KEY_CATEGORY = "category"
        const val KEY_AMOUNT = "amount"
        const val KEY_IDENTIFIER = "identifier"
    }

    override fun marshal(t: Benefit): Map<String, Any> {
        return mapOf(
                BenefitMapper.KEY_CATEGORY to t.category,
                BenefitMapper.KEY_AMOUNT to t.amount,
                BenefitMapper.KEY_IDENTIFIER to t.identifier
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
