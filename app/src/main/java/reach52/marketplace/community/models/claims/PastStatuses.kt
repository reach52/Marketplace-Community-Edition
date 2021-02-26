package reach52.marketplace.community.models.claims

import org.threeten.bp.ZonedDateTime

data class PastStatuses(
        val status: String,
        val username: String,
        val userID: String,
        val dateOfStatus: ZonedDateTime
)