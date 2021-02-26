package reach52.marketplace.community.models

import org.threeten.bp.ZonedDateTime

data class EffectiveDate(
        val dateStart: ZonedDateTime,
        val dateEnd: ZonedDateTime
)
