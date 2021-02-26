package reach52.marketplace.community.models.claims

import org.threeten.bp.ZonedDateTime

data class BenefitClaims(
        val claimAmount: Double,
        val claimNotifiedDate: ZonedDateTime,
        val lastClaimSubmittedDate: ZonedDateTime,
        val userID: String
)