package reach52.marketplace.community.models.logistic_resident

import org.threeten.bp.ZonedDateTime

data class CreatedBy(
        val dateCreated: ZonedDateTime,
        val username: String,
        val userId: String,
        val userDisplayName: String
)
