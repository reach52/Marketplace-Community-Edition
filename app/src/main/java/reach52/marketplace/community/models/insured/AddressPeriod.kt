package reach52.marketplace.community.models.insured

import arrow.core.Option
import org.threeten.bp.ZonedDateTime

data class AddressPeriod(
        val start: ZonedDateTime,
        val end: Option<ZonedDateTime>
)
