package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Formulary
import reach52.marketplace.community.persistence.Unmarshaler


@Suppress("UNCHECKED_CAST")
class FormularyMapper : Unmarshaler<Formulary>{
    companion object {
        const val KEY_BENEFICIARY = "beneficiary"
        const val KEY_BENEFITS = "benefits"
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_RATE = "rate"
        const val KEY_CATEGORY = "category"
        const val KEY_SUMMARY = "summary"
        const val KEY_TIER = "tier"
        const val KEY_AGE_RANGE = "ageRange"

        val benefitsMapper = BenefitsMapper()
        val ageRangeMapper = AgeRangeMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): Formulary {
        return Formulary(
                beneficiary = (properties[KEY_BENEFICIARY] as Number).toInt(),
                benefits = (properties[KEY_BENEFITS] as List<*>)
                        .filterIsInstance<Map<String, Any>>()
                        .map(benefitsMapper::unmarshal),
                identifier = properties[KEY_IDENTIFIER] as String,
                rate = (properties[KEY_RATE] as Number).toDouble(),
                summary = properties[KEY_SUMMARY] as String,
                tier = properties[KEY_TIER] as String,
                category = properties[KEY_CATEGORY] as String,
                ageRange = ageRangeMapper.unmarshal(properties[KEY_AGE_RANGE] as Map<String, Any>)
        )
    }
}
