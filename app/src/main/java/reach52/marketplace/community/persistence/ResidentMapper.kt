package reach52.marketplace.community.persistence

import arrow.core.None
import arrow.core.Option
import reach52.marketplace.community.extensions.catOptions
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.insured.Address
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

class ResidentMapper : Unmarshaler<Resident> {
    companion object {
        const val KEY_ADDRESS_1: String = "Address_1"
        const val KEY_ADDRESS_2: String = "Address_2"
        const val KEY_ANSWERS = "answers"
        const val KEY_BIRTH_DATE: String = "DoB"
        const val KEY_COUNTRY: String = "countryName"
        const val KEY_FIRST_NAME: String = "First_Name"
        const val KEY_GENDER: String = "Gender"
        const val KEY_ID: String = "_id"
        const val KEY_LAST_NAME: String = "Last_Name"
        const val KEY_MIDDLE_NAME: String = "Middle_Name"
        const val KEY_POSTAL_CODE: String = "postalCode"
        const val KEY_PROVINCE_CITY: String = "provinceCity"
        const val KEY_MARITAL_STATUS: String = "maritalStatus"
        const val TYPE_RESIDENT: String = "profile"
        const val KEY_EMAIL: String = "emailAddress"
        const val KEY_CONTACT: String = "contact"
    }

    override fun unmarshal(properties: Map<String, Any>): Resident {
        @Suppress("UNCHECKED_CAST")
        val answers = properties[KEY_ANSWERS] as Map<String, Any>
        val birthDate = LocalDateTime.ofInstant(Instant.parse(answers[KEY_BIRTH_DATE] as String), ZoneOffset.UTC).toLocalDate()
        val firstName = Option.fromNullable(answers[KEY_FIRST_NAME] as? String)
        val lastName = Option.fromNullable(answers[KEY_LAST_NAME] as? String)
        val middleName = Option.fromNullable(answers[KEY_MIDDLE_NAME] as? String)

        val line = listOf(Option.fromNullable(answers[KEY_ADDRESS_1] as? String),
                Option.fromNullable(answers[KEY_ADDRESS_2] as? String)).catOptions()
        val address = Address(
                city = Option.fromNullable(answers[KEY_PROVINCE_CITY] as? String),
                country = Option.fromNullable(answers[KEY_COUNTRY] as? String),
                district = None,
                line = line,
                postalCode = Option.fromNullable(answers[KEY_POSTAL_CODE] as? String),
                state = None
        )
        val maritalStatus = Option.fromNullable(answers[KEY_MARITAL_STATUS] as? String)
        val email = Option.fromNullable(answers[KEY_EMAIL] as? String)
        val contact = Option.fromNullable(answers[KEY_CONTACT] as? String)

        return Resident(
                address = address,
                birthDate = birthDate,
                firstName = firstName,
                gender = answers[KEY_GENDER] as String,
                id = properties[KEY_ID] as String,
                lastName = lastName,
                maritalStatus = maritalStatus,
                middleName = middleName,
                email = email,
                contact = contact
        )
    }
}