package reach52.marketplace.community.models.medication

import arrow.core.Option
import org.threeten.bp.ZonedDateTime

data class OrderStatus(
        val reason: Option<String>,
        val status: Status,
        val statusDate: ZonedDateTime,
        val userDisplayName: Option<String>,
        val userId: Option<String>,
        val username: Option<String>
) {
    enum class Status {
        ACCEPTED,
        DELIVERED,
        DISPATCHED,
        NOT_DELIVERED,
        PENDING,
        PICKED_UP,
        RECEIVED,
        REJECTED,
        ON_HOLD
    }
}