package reach52.marketplace.community.persistence.insured

import arrow.core.Some
import reach52.marketplace.community.models.*
import reach52.marketplace.community.models.insured.Address
import reach52.marketplace.community.persistence.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsuredMapperTest {
    private val insuredModelFixture = Insured(
            documentMeta = DocumentMeta(
                    dateCreated = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                    createdBy = "name",
                    organization = "organization",
                    source = "source",
                    type = "type"
            ),
            domainResource = InsuredDomainResource(
                    identifier = "12ad332a3d3823",
                    resourceType = "Insurance Owner",
                    address = Address(
                            city = Some("line"),
                            country = Some("Philippines"),
                            district = Some("District 10"),
                            line = listOf("line"),
                            postalCode = Some("3017"),
                            state = Some("State 03")
                    ),
                    dateOfBirth = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                    civilStatus = "single",
                    gender = "female",
                    contact = "0987654321",
                    subject = Subject(
                            profileID = "Resident.id",
                            reference = "Resident.id",
                            firstName = "first",
                            lastName = "last",
                            middleName = "middle"
                    ),
                    period = CoveragePeriod(
                            coverageStart = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                            coverageEnd = ZonedDateTime.of(2020, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC)
                    ),
                    plan = listOf(PlanReference(
                            title = "Plan Reference Title",
                            price = 12.00,
                            planReference = "Plan Reference",
                            planOwner = "Plan Owner",
                            planOwnerName = "Plan Owner Name",
                            currentStatus = Status(
                                    status = "status",
                                    statusDate = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                                    username = "Username",
                                    usernameId = "Username ID",
                                    userDisplayName = "User Display Name"
                            ),
                            pastStatuses = listOf(
                                    Status(
                                            status = "status",
                                            statusDate = ZonedDateTime.parse("2019-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                                            username = "Username",
                                            usernameId = "Username ID",
                                            userDisplayName = "User Display Name"
                                    )
                            ),
                            specificCost = listOf(
                                    SpecificCost(
                                            identifier = "specific cost identifier",
                                            category = "category",
                                            cost = 12.00
                                    )
                            ),
                            effectiveDate = EffectiveDate(
                                    dateStart = ZonedDateTime.of(2019, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                                    dateEnd = ZonedDateTime.of(2020, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC)
                            )
                    )),
                    coverage = listOf(CoverageInsured(
                            beneficiary = "beneficiary",
                            dependent = "dependent",
                            relationship = "relationship"
                    )),
                    emailAddress = "email@email.com"
            ),
            type = InsuredMapper.KEY_INSURANCE_OWNER
    )

    private val insuredMapFixture = mapOf(
            InsuredMapper.KEY_DOCUMENT_META to mapOf(
                    DocumentMetaMapper.KEY_DOCUMENT_META_DATE_CREATED to "2019-07-05T00:00:00Z",
                    DocumentMetaMapper.KEY_DOCUMENT_META_CREATED_BY to "name",
                    DocumentMetaMapper.KEY_DOCUMENT_META_ORGANIZATION to "organization",
                    DocumentMetaMapper.KEY_DOCUMENT_META_SOURCE to "source",
                    DocumentMetaMapper.KEY_DOCUMENT_META_TYPE to "type"
            ),
            InsuredMapper.KEY_DOMAIN_RESOURCE to mapOf(
                    InsuredDomainResourceMapper.KEY_INSURED_ID to "12ad332a3d3823",
                    InsuredDomainResourceMapper.KEY_RESOURCE_TYPE to "Insurance Owner",
                    InsuredDomainResourceMapper.KEY_INSURED_ADDRESS to mapOf(
                            AddressMapper.KEY_CITY to "line",
                            AddressMapper.KEY_COUNTRY to "Philippines",
                            AddressMapper.KEY_DISTRICT to "District 10",
                            AddressMapper.KEY_LINE to listOf("line"),
                            AddressMapper.KEY_POSTAL_CODE to "3017",
                            AddressMapper.KEY_STATE to "State 03"
                    ),
                    InsuredDomainResourceMapper.KEY_INSURED_DATE_OF_BIRTH to "2019-07-05T00:00:00Z",
                    InsuredDomainResourceMapper.KEY_INSURED_CIVIL_STATUS to "single",
                    InsuredDomainResourceMapper.KEY_INSURED_GENDER to "female",
                    InsuredDomainResourceMapper.KEY_INSURED_CONTACT to "0987654321",
                    InsuredDomainResourceMapper.KEY_INSURED_SUBJECT to mapOf(
                            SubjectMapper.KEY_SUBJECT_PROFILE_ID to "Resident.id",
                            SubjectMapper.KEY_SUBJECT_REFERENCE to "Resident.id",
                            SubjectMapper.KEY_SUBJECT_FIRST_NAME to "first",
                            SubjectMapper.KEY_SUBJECT_LAST_NAME to "last",
                            SubjectMapper.KEY_SUBJECT_MIDDLE_NAME to "middle"
                    ),
                    InsuredDomainResourceMapper.KEY_COVERAGE_PERIOD to mapOf(
                            CoveragePeriodMapper.KEY_COVERAGE_START to "2019-07-05T00:00:00Z",
                            CoveragePeriodMapper.KEY_COVERAGE_END to "2020-07-05T00:00:00Z"
                    ),
                    InsuredDomainResourceMapper.KEY_INSURED_PLAN to listOf(
                            mapOf(
                                    PlanReferenceMapper.KEY_PLAN_TITLE to "Plan Reference Title",
                                    PlanReferenceMapper.KEY_PLAN_PRICE to 12.00,
                                    PlanReferenceMapper.KEY_PLAN_REFERENCE to "Plan Reference",
                                    PlanReferenceMapper.KEY_PLAN_OWNER to "Plan Owner",
                                    PlanReferenceMapper.KEY_PLAN_OWNER_NAME to "Plan Owner Name",
                                    PlanReferenceMapper.KEY_PLAN_CURRENT_STATUS to mapOf(
                                            InsuredStatusMapper.KEY_STATUS to "status",
                                            InsuredStatusMapper.KEY_STATUS_DATE to "2020-07-05T00:00Z",
                                            InsuredStatusMapper.KEY_USERNAME to "Username",
                                            InsuredStatusMapper.KEY_USERNAME_ID to "Username ID",
                                            InsuredStatusMapper.KEY_USER_DISPLAY_NAME to "User Display Name"
                                    ),
                                    PlanReferenceMapper.KEY_PLAN_PAST_STATUSES to listOf<Map<String, Any>>(
                                            mapOf(
                                                    InsuredStatusMapper.KEY_STATUS to "status",
                                                    InsuredStatusMapper.KEY_STATUS_DATE to "2019-07-05T00:00Z",
                                                    InsuredStatusMapper.KEY_USERNAME to "Username",
                                                    InsuredStatusMapper.KEY_USERNAME_ID to "Username ID",
                                                    InsuredStatusMapper.KEY_USER_DISPLAY_NAME to "User Display Name"
                                            )
                                    ),
                                    PlanReferenceMapper.KEY_PLAN_SPECIFIC_COST to listOf(
                                            mapOf(
                                                    SpecificCostMapper.KEY_IDENTIFIER to "specific cost identifier",
                                                    SpecificCostMapper.KEY_CATEGORY to "category",
                                                    SpecificCostMapper.KEY_COST to 12.00
                                            )
                                    ),
                                    PlanReferenceMapper.KEY_PLAN_EFFECTIVE_DATE to mapOf(
                                            EffectiveDateMapper.KEY_EFFECTIVE_DATE_START to "2019-07-05T00:00:00Z",
                                            EffectiveDateMapper.KEY_EFFECTIVE_DATE_END to "2020-07-05T00:00:00Z"
                                    ))
                    ),
                    InsuredDomainResourceMapper.KEY_INSURED_COVERAGE_PLAN to listOf(mapOf(
                            CoverageInsuredMapper.KEY_COVERAGE_BENEFICIARY to "beneficiary",
                            CoverageInsuredMapper.KEY_COVERAGE_DEPENDENT to "dependent",
                            CoverageInsuredMapper.KEY_COVERAGE_RELATIONSHIP to "relationship"
                    )),
                    InsuredDomainResourceMapper.KEY_EMAIL to "email@email.com"

            ),
            InsuredMapper.KEY_TYPE to InsuredMapper.KEY_INSURANCE_OWNER
    )

    @Test
    fun unmarshal() {
        assertEquals(insuredModelFixture, InsuredMapper().unmarshal(insuredMapFixture))
    }

    @Test
    fun marshal() {
        assertEquals(insuredMapFixture, InsuredMapper().marshal(insuredModelFixture))
    }
}