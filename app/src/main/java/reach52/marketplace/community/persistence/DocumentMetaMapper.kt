package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.DocumentMeta
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class DocumentMetaMapper : Marshaler<DocumentMeta>, Unmarshaler<DocumentMeta> {
    companion object {
        const val KEY_DOCUMENT_META_DATE_CREATED = "dateCreated"
        const val KEY_DOCUMENT_META_CREATED_BY = "createdBy"
        const val KEY_DOCUMENT_META_ORGANIZATION = "organization"
        const val KEY_DOCUMENT_META_SOURCE = "source"
        const val KEY_DOCUMENT_META_TYPE = "type"
    }

    override fun marshal(t: DocumentMeta): Map<String, Any> {
        return mapOf(
                KEY_DOCUMENT_META_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_DOCUMENT_META_CREATED_BY to t.createdBy,
                KEY_DOCUMENT_META_ORGANIZATION to t.organization,
                KEY_DOCUMENT_META_SOURCE to t.source,
                KEY_DOCUMENT_META_TYPE to t.type
        )
    }


    override fun unmarshal(properties: Map<String, Any>): DocumentMeta {
        val dateCreated = ZonedDateTime.parse(properties[KEY_DOCUMENT_META_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val createdBy = properties[KEY_DOCUMENT_META_CREATED_BY] as String
        val organization = properties[KEY_DOCUMENT_META_ORGANIZATION] as String
        val source = properties[KEY_DOCUMENT_META_SOURCE] as String
        val type = properties[KEY_DOCUMENT_META_TYPE] as String

        return DocumentMeta(
                dateCreated = dateCreated,
                createdBy = createdBy,
                organization = organization,
                source = source,
                type = type
        )
    }
}