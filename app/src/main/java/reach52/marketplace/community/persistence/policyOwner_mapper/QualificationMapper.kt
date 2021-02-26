package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Qualification
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class QualificationMapper: Marshaler<Qualification>, Unmarshaler<Qualification> {
    companion object{
        const val KEY_DATA = "data"
        const val KEY_REVIEWED_DATE = "reviewed_date"
        const val KEY_DENIAL_REASON = "denial_reason"
        const val KEY_IS_ACCEPTED = "isAccepted"
        const val KEY_REVIEWED_BY = "reviewedBy"
    }

    override fun marshal(t: Qualification): Map<String, Any> {
        return mapOf(
                KEY_DATA to t.data,
                KEY_REVIEWED_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.reviewedDate),
                KEY_IS_ACCEPTED to t.isAccepted,
                KEY_DENIAL_REASON to t.denialReason,
                KEY_REVIEWED_BY to t.reviewedBy
        )

    }

    override fun unmarshal(properties: Map<String, Any>): Qualification {
        val data = properties[KEY_DATA] as Map<String, Any>
        val reviewDate =  ZonedDateTime.parse(properties[KEY_REVIEWED_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val denialReason = properties[KEY_DENIAL_REASON] as String
        val isAccepted = properties[KEY_IS_ACCEPTED] as Boolean
        val reviewedBy = properties[KEY_REVIEWED_BY] as String

        return Qualification(
                data = data,
                reviewedDate = reviewDate,
                denialReason = denialReason,
                isAccepted = isAccepted,
                reviewedBy = reviewedBy

        )
    }
}