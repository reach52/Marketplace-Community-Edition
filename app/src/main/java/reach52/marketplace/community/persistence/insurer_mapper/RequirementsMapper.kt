package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Requirement
import reach52.marketplace.community.persistence.Unmarshaler

class RequirementsMapper: Unmarshaler<Requirement> {
    companion object {
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_CATEGORY = "category"
        const val KEY_OPTION = "option"
        const val KEY_VARIATIONS = "variations"
        const val KEY_REFERENCES = "references"
    }

    override fun unmarshal(properties: Map<String, Any>): Requirement {
        return Requirement(
                identifier = properties[KEY_IDENTIFIER] as String,
                category = properties[KEY_CATEGORY] as String,
                option = properties[KEY_OPTION] as Boolean,
                references = (properties[KEY_REFERENCES] as List<*>).map { it as String },
                variations = (properties[KEY_VARIATIONS] as List<*>).map { it as String }
        )
    }

}
