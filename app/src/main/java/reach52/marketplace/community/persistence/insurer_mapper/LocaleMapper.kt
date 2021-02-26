package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Locale
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class LocaleMapper : Unmarshaler<Locale>{
    companion object {
        const val KEY_PRICING = "pricing"
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_LANGUAGES = "languages"

        val pricingMapper = PricingMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): Locale {
        return Locale (
                pricing = pricingMapper.unmarshal(properties[KEY_PRICING] as Map<String, Any>),
                identifier = properties[KEY_IDENTIFIER] as String,
                languages = (properties[KEY_LANGUAGES] as List<*>).map { it as String }
        )
    }
}
