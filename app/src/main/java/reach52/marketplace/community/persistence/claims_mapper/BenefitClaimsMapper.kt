package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.BenefitClaims
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class BenefitClaimsMapper : Unmarshaler<BenefitClaims>, Marshaler<BenefitClaims> {
    companion object {
        const val KEY_CLAIM_AMOUNT = "claimAmount"
        const val KEY_CLAIM_NOTIFIED_DATE = "claimNotifiedDate"
        const val KEY_LAST_CLAIM_SUBMITTED_DATE = "lastClaimSubmittedDate"
        const val KEY_USER_ID = "userID"
    }

    override fun marshal(t: BenefitClaims): Map<String, Any> {
        return mapOf(
                KEY_CLAIM_AMOUNT to t.claimAmount,
                KEY_CLAIM_NOTIFIED_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.claimNotifiedDate),
                KEY_LAST_CLAIM_SUBMITTED_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.lastClaimSubmittedDate),
                KEY_USER_ID to t.userID
        )
    }

    override fun unmarshal(properties: Map<String, Any>): BenefitClaims {
        val claimAmount = (properties[KEY_CLAIM_AMOUNT] as Number).toDouble()
        val claimNotifiedDate = ZonedDateTime.parse(properties[KEY_CLAIM_NOTIFIED_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val lastClaimSubmittedDate = ZonedDateTime.parse(properties[KEY_LAST_CLAIM_SUBMITTED_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val userID = properties[KEY_USER_ID] as String

        return BenefitClaims(
                claimAmount = claimAmount,
                claimNotifiedDate = claimNotifiedDate,
                lastClaimSubmittedDate = lastClaimSubmittedDate,
                userID = userID
        )
    }
}