package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Insured
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsuredMapper: Marshaler<Insured>, Unmarshaler<Insured> {
    companion object{
        const val KEY_REFERENCE_INSURED = "reference"
        const val KEY_DISPLAY = "display"
        const val KEY_ADDRESS = "address"
        const val KEY_EMAIL = "email"
        const val KEY_CONTACT = "contact"
        const val KEY_CIVIL_STATUS = "civilStatus"
        const val KEY_GENDER = "gender"
        const val KEY_BIRTH_DATE = "dateOfBirth"
        const val KEY_RELATIONSHIP = "relationship"
    }

    override fun marshal(t: Insured): Map<String, Any> {
        return mapOf(
                KEY_REFERENCE_INSURED to t.reference,
                KEY_DISPLAY to t.display,
                KEY_ADDRESS to t.address,
                KEY_EMAIL to t.email,
                KEY_CONTACT to t.contact,
                KEY_CIVIL_STATUS to t.civilStatus,
                KEY_GENDER to t.gender,
                KEY_BIRTH_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateOfBirth),
                KEY_RELATIONSHIP to t.relationship
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Insured {
        val reference = properties[KEY_REFERENCE_INSURED] as String
        val display = properties[KEY_DISPLAY] as String
        val relationship = properties[KEY_RELATIONSHIP] as String

        return Insured(
                reference = reference,
                display = display,
                address = properties[KEY_ADDRESS] as String,
                email = properties[KEY_EMAIL] as String,
                contact = properties[KEY_CONTACT] as String,
                civilStatus = properties[KEY_CIVIL_STATUS] as String,
                gender = properties[KEY_GENDER] as String,
                dateOfBirth = ZonedDateTime.parse(properties[KEY_BIRTH_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                relationship = relationship
        )
    }
}