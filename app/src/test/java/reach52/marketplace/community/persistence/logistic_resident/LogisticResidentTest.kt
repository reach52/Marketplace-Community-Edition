package reach52.marketplace.community.persistence.logistic_resident

import reach52.marketplace.community.models.logistic_resident.CreatedBy
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.persistence.logistic_mapper.CreatedByMapper
import reach52.marketplace.community.resident.mapper.LogisticResidentMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class LogisticResidentTest {
    private val logisticResidentModelFixture = LogisticResident(
            residentId = "12ad332a3d3823",
            type = "dispergoProfile",
            source = "dispergo",
            firstName = "Sam",
            middleName = "Son",
            lastName = "Simpson",
            email = "sam@reach52.com",
            gender = "male",
            maritalStatus = "single",
            contact = "987654321",
            dateOfBirth = ZonedDateTime.parse("1990-03-11T00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            address_1 = "Mercedes",
            address_2 = "North Park",
            postalCode = "3330",
            provinceCity = "Manila",
            country = "Philippines",
            createdBy = CreatedBy(
                    dateCreated = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    username = "juan@reach52.com",
                    userId = "juan@reach52.com",
                    userDisplayName = "Juan Dela Cruz"
            ),
            updatedBy = listOf(CreatedBy(
                    dateCreated = ZonedDateTime.parse("2020-08-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    username = "juan@reach52.com",
                    userId = "juan@reach52.com",
                    userDisplayName = "Juan Dela Cruz"
            ))
    )

    private val logisticResidentMapFixture = mapOf(
            LogisticResidentMapper.KEY_RESIDENT_ID to "12ad332a3d3823",
            LogisticResidentMapper.KEY_TYPE to "dispergoProfile",
            LogisticResidentMapper.KEY_SOURCE to "dispergo",
            LogisticResidentMapper.KEY_FIRST_NAME to "Sam",
            LogisticResidentMapper.KEY_MIDDLE_NAME to "Son",
            LogisticResidentMapper.KEY_LAST_NAME to "Simpson",
            LogisticResidentMapper.KEY_EMAIL to "sam@reach52.com",
            LogisticResidentMapper.KEY_GENDER to "male",
            LogisticResidentMapper.KEY_MARITAL_STATUS to "single",
            LogisticResidentMapper.KEY_CONTACT to "987654321",
            LogisticResidentMapper.KEY_DATE_OF_BIRTH to "1990-03-11T00:00Z",
            LogisticResidentMapper.KEY_ADDRESS_1 to "Mercedes",
            LogisticResidentMapper.KEY_ADDRESS_2 to "North Park",
            LogisticResidentMapper.KEY_POSTAL_CODE to "3330",
            LogisticResidentMapper.KEY_PROVINCE_CITY to "Manila",
            LogisticResidentMapper.KEY_COUNTRY to "Philippines",
            LogisticResidentMapper.KEY_CREATED_BY to mapOf(
                    CreatedByMapper.KEY_DATE_CREATED to "2020-07-05T00:00Z",
                    CreatedByMapper.KEY_USERNAME to "juan@reach52.com",
                    CreatedByMapper.KEY_USER_ID to "juan@reach52.com",
                    CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
            ),
            LogisticResidentMapper.KEY_UPDATED_BY to listOf(
                    mapOf(
                            CreatedByMapper.KEY_DATE_CREATED to "2020-08-05T00:00Z",
                            CreatedByMapper.KEY_USERNAME to "juan@reach52.com",
                            CreatedByMapper.KEY_USER_ID to "juan@reach52.com",
                            CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
                    )
            )
    )

    @Test
    fun marshal() {
        Assert.assertEquals(logisticResidentMapFixture, LogisticResidentMapper().marshal(logisticResidentModelFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(logisticResidentModelFixture, LogisticResidentMapper().unmarshal(logisticResidentMapFixture))
    }
}