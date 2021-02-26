package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.RequirementReference

class RequirementReferenceMapper : Marshaler<RequirementReference>, Unmarshaler<RequirementReference> {
    companion object {
        const val KEY_REQ_REF_COMPLETED = "completed"
        const val KEY_REQ_REF_VALUE = "value"
        const val KEY_REQ_REF = "requirementReference"
    }

    override fun marshal(t: RequirementReference): Map<String, Any> = mapOf(
            KEY_REQ_REF_COMPLETED to t.completed,
            KEY_REQ_REF_VALUE to t.value,
            KEY_REQ_REF to t.requirementReference

    )

    override fun unmarshal(properties: Map<String, Any>): RequirementReference {
        val completed = properties[KEY_REQ_REF_COMPLETED] as Boolean
        val value = properties[KEY_REQ_REF_VALUE] as String
        val requirementReference = properties[KEY_REQ_REF] as String

        return RequirementReference(
                completed = completed,
                value = value,
                requirementReference = requirementReference
        )
    }
}