package reach52.marketplace.community.persistence.consumerHealth_mapper

import reach52.marketplace.community.models.consumer_health_order.CreatedBy
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class CreatedByMapper: Unmarshaler<CreatedBy>, Marshaler<CreatedBy> {
    companion object{
        const val KEY_USER_ID = "userId"
        const val KEY_USER_DISPLAY_NAME = "userDisplayName"
    }

    override fun marshal(t: CreatedBy): Map<String, Any> {
        return mutableMapOf(
                KEY_USER_ID to t.userId,
                KEY_USER_DISPLAY_NAME to t.userDisplayName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CreatedBy {
        val userId = properties[KEY_USER_ID] as String
        val userDisplayName = properties[KEY_USER_DISPLAY_NAME] as String

        return CreatedBy(
                userId = userId,
                userDisplayName = userDisplayName
        )
    }
}