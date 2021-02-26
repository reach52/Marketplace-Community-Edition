package reach52.marketplace.community.models

import org.threeten.bp.ZonedDateTime

data class DocumentMeta(
        val dateCreated: ZonedDateTime,
        val createdBy: String,
        val organization: String,
        val source: String,
        val type: String
)