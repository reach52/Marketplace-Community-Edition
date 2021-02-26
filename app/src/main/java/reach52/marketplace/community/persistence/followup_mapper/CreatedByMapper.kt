package reach52.marketplace.community.persistence.followup_mapper

import reach52.marketplace.community.models.follow_up.CreatedBy
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class CreatedByMapper : Marshaler<CreatedBy>, Unmarshaler<CreatedBy>{
    companion object {
        const val KEY_USER_ID = "userId"
        const val KEY_USER_DISPLAY_NAME = "userDisplayName"
    }

    override fun marshal(t: CreatedBy): Map<String, Any> {
        return mapOf(
                KEY_USER_ID to t.userId,
                KEY_USER_DISPLAY_NAME to t.userDisplayName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CreatedBy = CreatedBy(
            userId = properties[KEY_USER_ID] as String,
            userDisplayName = properties[KEY_USER_DISPLAY_NAME] as String
    )
}