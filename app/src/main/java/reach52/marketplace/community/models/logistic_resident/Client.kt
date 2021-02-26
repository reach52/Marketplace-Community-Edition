package reach52.marketplace.community.models.logistic_resident

import arrow.core.Option
import arrow.core.getOrElse
import com.couchbase.lite.Database
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.models.User
import reach52.marketplace.community.resident.mapper.LogisticResidentMapper
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class Client(private val database: Database) {

    private val mapper = LogisticResidentMapper()

    fun logisticResidentMap(
            user: Option<User>,
            residentId: Option<String>,
            firstName: String,
            middleName: String,
            lastName: String,
            email: String,
            gender: String,
            maritalStatus: String,
            contact: String,
            dateOfBirth: ZonedDateTime,
            address: String,
            provinceCity: String,
            country: String,
            postalCode: String
    ): LogisticResident {
        val createdBy = CreatedBy(
                dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                username = user.map { it.username }.getOrElse { "" },
                userId = user.map { it.username }.getOrElse { "" },
                userDisplayName = user.map { it.displayName }.getOrElse { "" }
        )

        return LogisticResident(
                residentId = residentId.getOrElse { "" },
                type = "dispergoProfile",
                source = "dispergo-android",
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                email = email,
                gender = gender,
                maritalStatus = maritalStatus,
                contact = contact,
                dateOfBirth = dateOfBirth,
                address_1 = "",
                address_2 = address,
                provinceCity = provinceCity,
                postalCode = postalCode,
                country = country,
                createdBy = createdBy,
                updatedBy = listOf()
        )
    }

    fun new(
            user: Option<User>,
            resident: LogisticResident
    ): String {
        return database.createDocument().run {

            val country = CountryManager.getCountryCodeOf(resident.country)

            val createdBy = CreatedBy(
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    username = user.map { it.username }.getOrElse { "" },
                    userId = user.map { it.username }.getOrElse { "" },
                    userDisplayName = user.map { it.displayName }.getOrElse { "" }
            )

            val logisticResident = LogisticResident(
                    residentId = id,
                    type = "dispergoProfile",
                    source = "dispergo-android",
                    firstName = resident.firstName,
                    middleName = resident.middleName,
                    lastName = resident.lastName,
                    email = resident.email,
                    gender = resident.gender,
                    maritalStatus = resident.maritalStatus,
                    contact = resident.contact,
                    dateOfBirth = resident.dateOfBirth,
                    address_1 = "",
                    address_2 = resident.address_2,
                    postalCode = resident.postalCode,
                    provinceCity = resident.provinceCity,
                    country = country!!,
                    createdBy = createdBy,
                    updatedBy = listOf()
            )

            putProperties(LogisticResidentMapper().marshal(logisticResident))
            id
        }

    }

    fun get(residentId: String): LogisticResident = mapper.unmarshal(database.getDocument(residentId).properties)

    fun updateResident(
            user: Option<User>,
            resident: LogisticResident
    ) {
        database.getDocument(resident.residentId).update {

            val country = CountryManager.getCountryCodeOf(resident.country)
            val logisticResident = mapper.unmarshal(it.properties)
            val createdBy = CreatedBy(
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    username = user.map { r -> r.username }.getOrElse { "" },
                    userId = user.map { r -> r.username }.getOrElse { "" },
                    userDisplayName = user.map { r -> r.displayName }.getOrElse { "" }
            )

            val updatedBy = logisticResident.updatedBy.toMutableList().apply { add(logisticResident.createdBy) }

            val lResident = logisticResident.copy(
                    residentId = resident.residentId,
                    firstName = resident.firstName,
                    middleName = resident.middleName,
                    lastName = resident.lastName,
                    email = resident.email,
                    gender = resident.gender,
                    maritalStatus = resident.maritalStatus,
                    contact = resident.contact,
                    dateOfBirth = resident.dateOfBirth,
                    address_2 = resident.address_2,
                    postalCode = resident.postalCode,
                    provinceCity = resident.provinceCity,
                    country = country!!,
                    createdBy = createdBy,
                    updatedBy = updatedBy
            )
            it.userProperties = mapper.marshal(lResident)

            true
        }
    }
}