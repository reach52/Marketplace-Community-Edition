package reach52.marketplace.community.models.follow_up

import org.threeten.bp.ZonedDateTime

data class FollowUp (
        val productId: String,
        val productName: String,
        val residentId: String,
        val residentName: String,
        val email: String,
        val contact: String,
        val status: String,
        val fbId: String,
        val reason: String,
        val notes: String,
        val address: String,
        val productDescription: String,
        val dateFollowUp: ZonedDateTime,
        val dateCreated: ZonedDateTime,
        val createdBy: CreatedBy,
        val type: String
)

