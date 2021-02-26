package reach52.marketplace.community.persistence

import arrow.core.Option
import reach52.marketplace.community.models.medication.OrderStatus
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class OrderStatusMapper : Marshaler<OrderStatus>, Unmarshaler<OrderStatus> {
    companion object {
        const val KEY_STATUS = "status"
        const val KEY_STATUS_DATE = "statusDate"
        const val KEY_USERNAME = "username"
        const val KEY_USER_DISPLAY_NAME = "userDisplayName"
        const val KEY_USER_ID = "userId"
        const val KEY_REASON = "reason"
    }

    override fun marshal(t: OrderStatus): Map<String, Any> = mutableMapOf(KEY_STATUS to t.status.toString(), KEY_STATUS_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.statusDate)).also {
        t.username.map { username ->
            it[KEY_USERNAME] = username
        }
        t.userDisplayName.map { userDisplayName ->
            it[KEY_USER_DISPLAY_NAME] = userDisplayName
        }
        t.userId.map { userId ->
            it[KEY_USER_ID] = userId
        }
        t.reason.map { reason ->
            it[KEY_REASON] = reason
        }
    }

    override fun unmarshal(properties: Map<String, Any>): OrderStatus = OrderStatus(
            reason = Option.fromNullable(properties[KEY_REASON]).map { it as String },
            status = enumValueOf((properties[KEY_STATUS] as String).toUpperCase(Locale.ROOT)),
            statusDate = ZonedDateTime.parse(properties[KEY_STATUS_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            username = Option.fromNullable(properties[KEY_USERNAME]).map { it as String },
            userDisplayName = Option.fromNullable(properties[KEY_USER_DISPLAY_NAME]).map { it as String },
            userId = Option.fromNullable(properties[KEY_USER_ID]).map { it as String }
    )
}