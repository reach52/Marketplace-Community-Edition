package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.EffectiveDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class EffectiveDateMapper : Marshaler<EffectiveDate>, Unmarshaler<EffectiveDate> {
    companion object {
        const val KEY_EFFECTIVE_DATE_START = "dateStart"
        const val KEY_EFFECTIVE_DATE_END = "dateEnd"
    }

    override fun marshal(t: EffectiveDate): Map<String, Any> {
        return mapOf(
                KEY_EFFECTIVE_DATE_START to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateStart),
                KEY_EFFECTIVE_DATE_END to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateEnd))
    }

    override fun unmarshal(properties: Map<String, Any>): EffectiveDate {
        val dateStart = ZonedDateTime.parse(properties[KEY_EFFECTIVE_DATE_START] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val dateEnd = ZonedDateTime.parse(properties[KEY_EFFECTIVE_DATE_END] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return EffectiveDate(
                dateStart = dateStart,
                dateEnd = dateEnd
        )
    }
}