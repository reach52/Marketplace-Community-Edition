package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.CoveragePeriod
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class CoveragePeriodMapper : Marshaler<CoveragePeriod>, Unmarshaler<CoveragePeriod> {
    companion object {
        const val KEY_COVERAGE_START = "dateStart"
        const val KEY_COVERAGE_END = "dateEnd"
    }

    override fun marshal(t: CoveragePeriod): Map<String, Any> {
        return mapOf(
                KEY_COVERAGE_START to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.coverageStart),
                KEY_COVERAGE_END to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.coverageEnd)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CoveragePeriod {
        val coverageStart = ZonedDateTime.parse(properties[KEY_COVERAGE_START] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val coverageEnd = ZonedDateTime.parse(properties[KEY_COVERAGE_END] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return CoveragePeriod(
                coverageStart = coverageStart,
                coverageEnd = coverageEnd
        )
    }
}