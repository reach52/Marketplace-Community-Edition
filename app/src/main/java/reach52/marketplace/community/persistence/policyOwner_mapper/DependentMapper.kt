package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.Dependent
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class DependentMapper: Marshaler<Dependent>, Unmarshaler<Dependent> {

    companion object{
        const val KEY_NAME = "full_name"
        const val KEY_DOB = "dob"
        const val KEY_RELATIONSHIP = "relationship"
        const val KEY_GENDER = "gender"
    }

    override fun marshal(t: Dependent): Map<String, Any> {
        return mapOf(
                KEY_NAME to t.fullName,
                KEY_DOB to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.DOB),
                KEY_RELATIONSHIP to t.relation,
                KEY_GENDER to t.gender
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Dependent {
        val fullName = properties[KEY_NAME] as String
        val DOB = ZonedDateTime.parse(properties[KEY_DOB] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val relationship = properties[KEY_RELATIONSHIP] as String
        val gender = properties[KEY_GENDER] as String

        return Dependent(
                fullName,
                DOB,
                relationship,
                gender
        )
    }
}