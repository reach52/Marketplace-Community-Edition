package reach52.marketplace.community.resident.mapper

import reach52.marketplace.community.models.logistic_resident.CreatedBy
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import reach52.marketplace.community.persistence.logistic_mapper.CreatedByMapper
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class LogisticResidentMapper : Marshaler<LogisticResident>, Unmarshaler<LogisticResident> {

    companion object {
        const val KEY_RESIDENT_ID = "residentId"
        const val KEY_TYPE = "type"
        const val KEY_SOURCE = "source"
        const val KEY_FIRST_NAME = "firstName"
        const val KEY_MIDDLE_NAME = "middleName"
        const val KEY_LAST_NAME = "lastName"
        const val KEY_EMAIL = "email"
        const val KEY_GENDER = "gender"
        const val KEY_MARITAL_STATUS = "maritalStatus"
        const val KEY_CONTACT = "contact"
        const val KEY_DATE_OF_BIRTH = "dateOfBirth"
        const val KEY_ADDRESS_1 = "Address_1"
        const val KEY_ADDRESS_2 = "Address_2"
        const val KEY_POSTAL_CODE = "postalCode"
        const val KEY_PROVINCE_CITY = "provinceCity"
        const val KEY_COUNTRY = "country"
        const val KEY_CREATED_BY = "createdBy"
        const val KEY_UPDATED_BY = "updatedBy"
        const val KEY_DISPERGO_RESIDENT = "dispergoProfile"

        val createdByMapper = CreatedByMapper()
    }

    override fun marshal(t: LogisticResident): Map<String, Any> {
        return mapOf(
                KEY_RESIDENT_ID to t.residentId,
                KEY_TYPE to KEY_DISPERGO_RESIDENT,
                KEY_SOURCE to t.source,
                KEY_FIRST_NAME to t.firstName,
                KEY_MIDDLE_NAME to t.middleName,
                KEY_LAST_NAME to t.lastName,
                KEY_EMAIL to t.email,
                KEY_GENDER to t.gender,
                KEY_MARITAL_STATUS to t.maritalStatus,
                KEY_CONTACT to t.contact,
                KEY_DATE_OF_BIRTH to t.dateOfBirth.toString(),
                KEY_ADDRESS_1 to t.address_1,
                KEY_ADDRESS_2 to t.address_2,
                KEY_POSTAL_CODE to t.postalCode,
                KEY_PROVINCE_CITY to t.provinceCity,
                KEY_COUNTRY to t.country,
                KEY_CREATED_BY to createdByMapper.marshal(t.createdBy),
                KEY_UPDATED_BY to t.updatedBy.map(createdByMapper::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): LogisticResident {
        return LogisticResident(
                residentId = properties[KEY_RESIDENT_ID] as String,
                type = properties[KEY_TYPE] as String,
                source = properties[KEY_SOURCE] as String,
                firstName = properties[KEY_FIRST_NAME] as String,
                middleName = properties[KEY_MIDDLE_NAME] as String,
                lastName = properties[KEY_LAST_NAME] as String,
                email = properties[KEY_EMAIL] as String,
                gender = properties[KEY_GENDER] as String,
                maritalStatus = properties[KEY_MARITAL_STATUS] as String,
                contact = properties[KEY_CONTACT] as String,
                dateOfBirth = ZonedDateTime.parse(properties[KEY_DATE_OF_BIRTH] as String,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                address_1 = properties[KEY_ADDRESS_1] as String,
                address_2 = properties[KEY_ADDRESS_2] as String,
                postalCode = properties[KEY_POSTAL_CODE] as String,
                provinceCity = properties[KEY_PROVINCE_CITY] as String,
                country = properties[KEY_COUNTRY] as String,
                createdBy = createdByMapper.unmarshal(properties[KEY_CREATED_BY] as
                        Map<String, Any>),
                updatedBy = (properties[KEY_UPDATED_BY] as List<CreatedBy>)
                        .filterIsInstance<Map<String, Any>>()
                        .map(createdByMapper::unmarshal)
        )
    }
}