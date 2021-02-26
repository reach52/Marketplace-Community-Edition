package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.Benefits
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class BenefitsMapper : Unmarshaler<Benefits>, Marshaler<Benefits> {

    companion object {
        const val KEY_BENEFITS_CLAIM_AMOUNT = "claimAmount"
        const val KEY_BENEFITS_NOTIFIED_DATE = "claimNotifiedDate"
    }

    override fun marshal(t: Benefits): Map<String, Any> {
        return mapOf(
                KEY_BENEFITS_CLAIM_AMOUNT to t.claimAmount,
                KEY_BENEFITS_NOTIFIED_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.notifiedDate)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Benefits {
        val benefitsClaimAmount = (properties[KEY_BENEFITS_CLAIM_AMOUNT] as Number).toDouble()
        val benefitsNotifiedDate = ZonedDateTime.parse(properties[KEY_BENEFITS_NOTIFIED_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return Benefits(
                claimAmount = benefitsClaimAmount,
                notifiedDate = benefitsNotifiedDate
        )
    }
}