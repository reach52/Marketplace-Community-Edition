package reach52.marketplace.community.persistence.medication_mapper

import reach52.marketplace.community.models.medication.Physician
import reach52.marketplace.community.persistence.Unmarshaler

class PhysicianMapper : Unmarshaler<Physician> {
    companion object {
        const val KEY_ID = "_id"
        const val KEY_LICENCE_NUMBER = "licenseNumber"
        const val KEY_NAME = "name"
        const val TYPE_PHYSICIAN = "physician"
    }

    override fun unmarshal(properties: Map<String, Any>) = Physician(
            id = properties[KEY_ID] as String,
            licenseNumber = properties[KEY_LICENCE_NUMBER] as String,
            name = properties[KEY_NAME] as String
    )
}