package reach52.marketplace.community.models.policy_owner

import org.threeten.bp.ZonedDateTime

data class Status (
        val status: String,
        val statusDate: ZonedDateTime,
        val username: String,
        val usernameId: String,
        val userDisplayName: String
)