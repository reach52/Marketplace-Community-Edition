package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.UpdatedBy
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class UpdatedByMapper: Marshaler<UpdatedBy>, Unmarshaler<UpdatedBy> {
    companion object{
        const val KEY_REFERENCE = "reference"
        const val KEY_DISPLAY = "display"
    }

    override fun marshal(t: UpdatedBy): Map<String, Any> {
        return mapOf(
                KEY_REFERENCE to t.reference,
                KEY_DISPLAY to t.display
        )
    }

    override fun unmarshal(properties: Map<String, Any>): UpdatedBy {
            val reference = properties[KEY_REFERENCE] as String
            val display = properties[KEY_DISPLAY] as String

        return UpdatedBy(
                reference = reference,
                display = display
        )
    }
}