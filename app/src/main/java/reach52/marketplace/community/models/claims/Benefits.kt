package reach52.marketplace.community.models.claims

import org.threeten.bp.ZonedDateTime

data class Benefits(
        val claimAmount: Double,
        val notifiedDate: ZonedDateTime
)