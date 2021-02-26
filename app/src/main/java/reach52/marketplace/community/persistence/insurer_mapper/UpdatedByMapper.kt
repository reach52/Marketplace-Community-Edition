package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.UpdatedBy
import reach52.marketplace.community.persistence.Unmarshaler

class UpdatedByMapper : Unmarshaler<UpdatedBy> {
    companion object {
        const val KEY_DISPLAY = "display"
        const val KEY_REFERENCE = "reference"
    }

    override fun unmarshal(properties: Map<String, Any>): UpdatedBy {
        return UpdatedBy(
                display = properties[KEY_DISPLAY] as String,
                reference = properties[KEY_REFERENCE] as String
        )
    }

}
