package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Questionnaire
import reach52.marketplace.community.persistence.Unmarshaler

class QuestionnaireMapper : Unmarshaler<Questionnaire>{
    companion object {
        const val KEY_URL = "url"
        const val KEY_KOBO_USER = "koboUser"
        const val KEY_REFERENCE = "reference"
    }

    override fun unmarshal(properties: Map<String, Any>): Questionnaire {
        return Questionnaire(
                reference = properties[KEY_REFERENCE] as String,
                url = properties[KEY_URL] as String,
                koboUser = properties[KEY_KOBO_USER] as String
        )
    }

}
