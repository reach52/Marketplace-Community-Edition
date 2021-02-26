package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.CurrentStatus
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsCurrentStatusMapper : Unmarshaler<CurrentStatus>, Marshaler<CurrentStatus> {
    companion object {
        const val KEY_STATUS = "status"
        const val KEY_CURRENT_STATUS_USERNAME = "username"
        const val KEY_CURRENT_STATUS_USER_ID = "userID"
        const val KEY_CURRENT_DATE_OF_STATUS = "dateOfStatus"
    }

    override fun marshal(t: CurrentStatus): Map<String, Any> {
        return mapOf(
                KEY_STATUS to t.status,
                KEY_CURRENT_STATUS_USERNAME to t.username,
                KEY_CURRENT_STATUS_USER_ID to t.userID,
                KEY_CURRENT_DATE_OF_STATUS to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateOfStatus)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CurrentStatus {
        val status = properties[KEY_STATUS] as String
        val username = properties[KEY_CURRENT_STATUS_USERNAME] as String
        val userID = properties[KEY_CURRENT_STATUS_USER_ID] as String
        val dateOfStatus = ZonedDateTime.parse(properties[KEY_CURRENT_DATE_OF_STATUS] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return CurrentStatus(
                status = status,
                username = username,
                userID = userID,
                dateOfStatus = dateOfStatus
        )
    }
}