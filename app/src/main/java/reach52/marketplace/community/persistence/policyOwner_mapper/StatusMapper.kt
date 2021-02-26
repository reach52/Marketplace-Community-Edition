package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Status
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class StatusMapper : Marshaler<Status>, Unmarshaler<Status> {

    companion object {
        const val KEY_STATUS = "status"
        const val KEY_STATUS_DATE = "statusDate"
        const val KEY_USERNAME = "username"
        const val KEY_USERNAME_ID = "userId"
        const val KEY_USER_DISPLAY_NAME = "userDisplayName"
    }

    override fun marshal(t: Status): Map<String, Any> {
        return mapOf(
                KEY_STATUS to t.status,
                KEY_STATUS_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.statusDate),
                KEY_USERNAME to t.username,
                KEY_USERNAME_ID to t.usernameId,
                KEY_USER_DISPLAY_NAME to t.userDisplayName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Status {
        val status = properties[KEY_STATUS] as String
        val statusDate = ZonedDateTime.parse(properties[KEY_STATUS_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val username = properties[KEY_USERNAME] as String
        val usernameId = properties[KEY_USERNAME_ID] as String
        val userDisplayName = properties[KEY_USER_DISPLAY_NAME] as String

        return Status(
                status = status,
                statusDate = statusDate,
                username = username,
                usernameId = usernameId,
                userDisplayName = userDisplayName
        )
    }

}