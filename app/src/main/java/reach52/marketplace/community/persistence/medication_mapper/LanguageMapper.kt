package reach52.marketplace.community.persistence.medication_mapper

import reach52.marketplace.community.models.medication.Language
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class LanguageMapper: Marshaler<Language>, Unmarshaler<Language>{
    companion object{
        const val KEY_TEXT = "text"
        const val KEY_LANGUAGE = "isoLanguage"
    }

    override fun marshal(t: Language): Map<String, Any> {
        return mapOf(
                KEY_TEXT to t.text,
                KEY_LANGUAGE to t.isoLanguage
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Language = Language(
        text = properties[KEY_TEXT] as String,
        isoLanguage = properties[KEY_LANGUAGE] as String
    )
}