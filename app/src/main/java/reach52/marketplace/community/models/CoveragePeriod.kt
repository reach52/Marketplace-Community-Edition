package reach52.marketplace.community.models

import org.threeten.bp.ZonedDateTime

data class CoveragePeriod(
        val coverageStart: ZonedDateTime,
        val coverageEnd: ZonedDateTime
)