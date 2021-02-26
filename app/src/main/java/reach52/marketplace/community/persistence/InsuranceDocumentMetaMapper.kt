package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.DocumentMeta
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsuranceDocumentMetaMapper : Marshaler<DocumentMeta>, Unmarshaler<DocumentMeta> {

    companion object {
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_CREATED_BY = "createdBy"
        const val KEY_ORGANIZATION = "organization"
        const val KEY_SOURCE = "source"
        const val KEY_TYPE = "type"
    }

    override fun marshal(t: DocumentMeta): Map<String, Any> {
        return mapOf(
                KEY_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_CREATED_BY to t.createdBy,
                KEY_ORGANIZATION to t.organization,
                KEY_SOURCE to t.source,
                KEY_TYPE to t.type
        )
    }

    override fun unmarshal(properties: Map<String, Any>): DocumentMeta {

        val dateCreated = ZonedDateTime.parse(properties[KEY_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val createdBy = properties[KEY_CREATED_BY] as String
        val organization = properties[KEY_ORGANIZATION] as String
        val source = properties[KEY_SOURCE] as String
        val type = properties[KEY_TYPE] as String

        return DocumentMeta(
                dateCreated = dateCreated,
                createdBy = createdBy,
                organization = organization,
                source = source,
                type = type
        )
    }
}