package reach52.marketplace.community.persistence

import arrow.core.None
import arrow.core.Option
import reach52.marketplace.community.models.insured.AddressPeriod
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class AddressPeriodMapper : Marshaler<AddressPeriod>, Unmarshaler<AddressPeriod> {
    companion object {
        const val KEY_START = "start"
        const val KEY_END = "end"
    }

    override fun marshal(t: AddressPeriod): Map<String, Any> =
            mutableMapOf<String, Any>(KEY_START to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.start)).also {
                t.end.map { end -> it[KEY_END] = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(end) }
            }

    override fun unmarshal(properties: Map<String, Any>): AddressPeriod {
        val start = ZonedDateTime.parse(properties[KEY_START] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return when (properties[KEY_END]) {
            "None", !is String -> AddressPeriod(
                    start = start,
                    end = None
            )
            else -> AddressPeriod(
                    start = start,
                    end = Option.fromNullable(
                            ZonedDateTime.parse(
                                    properties[KEY_END] as String,
                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                    )
            )
        }
    }
}
