package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.PastStatuses
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsPastStatusesMapper : Unmarshaler<PastStatuses>, Marshaler<PastStatuses> {
    companion object {
        const val KEY_PAST_STATUS = "pastStatuses"
        const val KEY_PAST_STATUS_USERNAME = "username"
        const val KEY_PAST_STATUS_USER_ID = "userID"
        const val KEY_PAST_STATUS_DATE_OF_STATUS = "dateOfStatus"
    }

    override fun marshal(t: PastStatuses): Map<String, Any> {
        return mapOf(
                KEY_PAST_STATUS to t.status,
                KEY_PAST_STATUS_USERNAME to t.username,
                KEY_PAST_STATUS_USER_ID to t.userID,
                KEY_PAST_STATUS_DATE_OF_STATUS to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateOfStatus)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): PastStatuses {
        val status = properties[KEY_PAST_STATUS] as String
        val username = properties[KEY_PAST_STATUS_USERNAME] as String
        val userID = properties[KEY_PAST_STATUS_USER_ID] as String
        val dateOfStatus = ZonedDateTime.parse(properties[KEY_PAST_STATUS_DATE_OF_STATUS] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return PastStatuses(
                status = status,
                username = username,
                userID = userID,
                dateOfStatus = dateOfStatus
        )
    }
}