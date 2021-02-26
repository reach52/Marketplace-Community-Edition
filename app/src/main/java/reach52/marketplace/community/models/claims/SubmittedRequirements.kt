package reach52.marketplace.community.models.claims

import org.threeten.bp.ZonedDateTime

data class SubmittedRequirements(
        val code: String,
        val type: String,
        val dateSubmitted: ZonedDateTime,
        val attachmentName: String
)