package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Pricing
import reach52.marketplace.community.persistence.Unmarshaler

class PricingMapper: Unmarshaler<Pricing> {

    companion object {
        const val KEY_TEXT = "text"
        const val KEY_UNIT = "unit"
    }

    override fun unmarshal(properties: Map<String, Any>): Pricing {
        return Pricing(
                text = properties[KEY_TEXT] as String,
                unit = properties[KEY_UNIT] as String
        )
    }
}
