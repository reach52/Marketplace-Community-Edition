package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.Requirement

class RequirementsInsuranceMapper : Unmarshaler<Requirement>, Marshaler<Requirement> {

    companion object {
        const val KEY_VALUE = "value"
        const val KEY_IDENTIFIER = "identifier"
    }

    override fun marshal(t: Requirement): Map<String, Any> {
        return mapOf(
                KEY_VALUE to t.value,
                KEY_IDENTIFIER to t.identifier
        )
    }


    override fun unmarshal(properties: Map<String, Any>): Requirement {

        val value = properties[KEY_VALUE] as String
        val identifier = properties[KEY_IDENTIFIER] as String

        return Requirement(
                value = value,
                identifier = identifier
        )
    }
}