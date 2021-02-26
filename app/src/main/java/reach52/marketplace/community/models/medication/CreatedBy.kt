package reach52.marketplace.community.models.medication

import org.threeten.bp.ZonedDateTime

data class CreatedBy(
        val dateTime: ZonedDateTime,
        val userId: String
)