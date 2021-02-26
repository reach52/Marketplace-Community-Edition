package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.InsurerDocument
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class InsurerDocumentMapper : Unmarshaler<InsurerDocument>{
    companion object {
        const val KEY_TYPE = "type"
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_DISPLAY = "display"
        const val KEY_ORGANIZATION_TYPE = "organizationType"
        const val KEY_SUMMARY = "summary"
        const val KEY_CONTACT = "contact"
        const val KEY_LOCALE = "locale"
        const val KEY_FORMULARY = "formulary"
        const val KEY_POLICY = "policy"
        const val KEY_REQUIREMENTS = "requirements"
        const val KEY_QUALIFICATION = "qualification"
        const val KEY_LAST_UPDATED = "dateLastUpdated"
        const val KEY_UPDATED_BY = "updatedBy"
        const val TYPE_INSURER = "insurer"

        val contactMapper = ContactMapper()
        val localeMapper = LocaleMapper()
        val formularyMapper = FormularyMapper()
        val policyMapper = PolicyMapper()
        val requirementsMapper = RequirementsMapper()
        val qualificationMapper = QualificationMapper()
        val updatedByMapper = UpdatedByMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): InsurerDocument {
        return InsurerDocument(
                identifier = properties[KEY_IDENTIFIER] as String,
                display = properties[KEY_DISPLAY] as String,
                summary = properties[KEY_SUMMARY] as String,
                type = properties[KEY_TYPE] as String,
                dateLastUpdated = ZonedDateTime.parse(properties[KEY_LAST_UPDATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                contact = contactMapper.unmarshal(properties[KEY_CONTACT] as Map<String, Any>),
                formulary = (properties[KEY_FORMULARY] as List<*>)
                        .filterIsInstance<Map<String,Any>>()
                        .map(formularyMapper::unmarshal),
                locale = localeMapper.unmarshal(properties[KEY_LOCALE] as Map<String, Any>),
                policy = policyMapper.unmarshal(properties[KEY_POLICY] as Map<String, Any>),
                qualification = qualificationMapper.unmarshal(properties[KEY_QUALIFICATION] as Map<String, Any>),
                requirements = (properties[KEY_REQUIREMENTS] as List<*>)
                        .filterIsInstance<Map<String,Any>>()
                        .map(requirementsMapper::unmarshal),
                updatedBy = updatedByMapper.unmarshal(properties[KEY_UPDATED_BY] as Map<String, Any>),
                organizationType = properties[KEY_ORGANIZATION_TYPE] as String
        )
    }
}
