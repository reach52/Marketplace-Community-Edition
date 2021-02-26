package reach52.marketplace.community.persistence.medication_mapper

import reach52.marketplace.community.models.medication.CreatedBy
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class CreatedByMapper: Marshaler<CreatedBy>, Unmarshaler<CreatedBy>{
    companion object{
        const val KEY_DATE_TIME = "dateTime"
        const val KEY_USER_ID = "userId"
    }

    override fun marshal(t: CreatedBy): Map<String, Any> {
        return mapOf(
                KEY_DATE_TIME to t.dateTime.toString(),
                KEY_USER_ID to t.userId
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CreatedBy = CreatedBy(
            dateTime = ZonedDateTime.parse(properties[KEY_DATE_TIME] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            userId = properties[KEY_USER_ID] as String
    )
}