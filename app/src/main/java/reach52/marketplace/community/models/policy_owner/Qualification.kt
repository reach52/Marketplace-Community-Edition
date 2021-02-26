package reach52.marketplace.community.models.policy_owner

import org.threeten.bp.ZonedDateTime

data class Qualification(
        var data: Map<String, Any>,
        val reviewedDate: ZonedDateTime,
        val denialReason: String,
        val isAccepted: Boolean,
        val reviewedBy: String
)