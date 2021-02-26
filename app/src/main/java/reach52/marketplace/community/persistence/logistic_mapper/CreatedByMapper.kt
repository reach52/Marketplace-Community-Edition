package reach52.marketplace.community.persistence.logistic_mapper

import reach52.marketplace.community.models.logistic_resident.CreatedBy
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class CreatedByMapper : Marshaler<CreatedBy>, Unmarshaler<CreatedBy> {
    companion object {
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_USERNAME = "username"
        const val KEY_USER_ID = "userId"
        const val KEY_USER_DISPLAY_NAME = "userDisplayName"
    }

    override fun marshal(t: CreatedBy): Map<String, Any> {
        return mapOf(
                KEY_DATE_CREATED to t.dateCreated.toString(),
                KEY_USERNAME to t.username,
                KEY_USER_ID to t.userId,
                KEY_USER_DISPLAY_NAME to t.userDisplayName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CreatedBy = CreatedBy(
            dateCreated = ZonedDateTime.parse(properties[KEY_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            username = (properties[KEY_USERNAME] as String),
            userId = (properties[KEY_USER_ID] as String),
            userDisplayName = (properties[KEY_USER_DISPLAY_NAME] as String)
    )
}