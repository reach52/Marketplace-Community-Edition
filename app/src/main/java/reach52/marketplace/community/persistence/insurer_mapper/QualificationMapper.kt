package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Qualification
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class QualificationMapper : Unmarshaler<Qualification>{
    companion object {
        const val KEY_QUESTIONNAIRE = "questionnaire"

        val questionnaireMapper = QuestionnaireMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): Qualification {
        return Qualification(
                questionnaire = questionnaireMapper.unmarshal(properties[KEY_QUESTIONNAIRE] as Map<String, Any>)
        )
    }

}
