package reach52.marketplace.community.persistence.policyOwner

import reach52.marketplace.community.models.insurance.Benefit
import reach52.marketplace.community.models.policy_owner.*
import reach52.marketplace.community.persistence.policyOwner_mapper.*
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class PolicyOwnerMapperTest {

    private val mapper = PolicyOwnerMapper()
    private val policyOwnerFixture = PolicyOwner(
            type = "type",
            policyOwnerID = "reference",
            policyOwnerFullName = "full name",
            address = "Pototan Iloilo",
            email = "test@email.com",
            contact = "0981213981",
            civilStatus = "single",
            gender = "male",
            dateOfBirth = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            insured = Insured(
                    reference = "reference",
                    display = "display",
                    address = "Pototan Iloilo",
                    email = "test@email.com",
                    contact = "0981213981",
                    civilStatus = "single",
                    gender = "male",
                    dateOfBirth = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    relationship = "Father"
            ),
            beneficiaries = listOf(Beneficiaries(
                    reference = "reference",
                    display = "display",
                    relationship = "Father"
            )),
            insurer = Insurer(
                    insurerName = "Malayan",
                    reference = "reference",
                    certificateNumber = "123",
                    unit = "KHR",
                    plan = listOf(Plan(
                            identifier = "7Y3OhYkpCx",
                            tier = "Health & Personal Accident Plan",
                            rate = 98568.00,
                            category = "adult",
                            benefits = listOf(Benefit(
                                    category = "Hospitalization without major surgery (Public health facilities)",
                                    amount = 0.00,
                                    identifier = "HwOmS_PHF"
                            ))
                    )),
                    attachments =  listOf("category1"),
                    qualification =  Qualification(
                            data =  mapOf("a" to "b"),
                            reviewedDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                            denialReason = "reason",
                            reviewedBy = "someone",
                            isAccepted = true
                    )
            ),
            status = "status",
            application = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            effectiveDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            expiry = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            lastUpdated = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            createdBy = "joseph@gmail.com",
            updatedBy =  UpdatedBy(
                    reference = "reference",
                    display = "display"
            ),
            pastStatuses = listOf(Status(
                    status = "active",
                    statusDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    username = "joseph@gmail.com",
                    usernameId = "1234",
                    userDisplayName = "joseph@gmail.com"
            )),
            dependents = null
    )

    private val mapFixture = mapOf(
            PolicyOwnerMapper.KEY_TYPE to "type",
            PolicyOwnerMapper.KEY_POLICY_OWNER_ID to "reference",
            PolicyOwnerMapper.KEY_POLICY_OWNER_NAME to "full name",
            PolicyOwnerMapper.KEY_ADDRESS to "Pototan Iloilo",
            PolicyOwnerMapper.KEY_EMAIL to "test@email.com",
            PolicyOwnerMapper.KEY_CONTACT to "0981213981",
            PolicyOwnerMapper.KEY_CIVIL_STATUS to "single",
            PolicyOwnerMapper.KEY_GENDER to "male",
            PolicyOwnerMapper.KEY_CREATED_BY to "joseph@gmail.com",
            PolicyOwnerMapper.KEY_BIRTH_DATE to "1970-01-01T00:00:00Z",
            PolicyOwnerMapper.KEY_INSURED to mapOf(
                    InsuredMapper.KEY_REFERENCE_INSURED to "reference",
                    InsuredMapper.KEY_DISPLAY to "display",
                    InsuredMapper.KEY_ADDRESS to "Pototan Iloilo",
                    InsuredMapper.KEY_EMAIL to "test@email.com",
                    InsuredMapper.KEY_CONTACT to "0981213981",
                    InsuredMapper.KEY_CIVIL_STATUS to "single",
                    InsuredMapper.KEY_GENDER to "male",
                    InsuredMapper.KEY_BIRTH_DATE to "1970-01-01T00:00:00Z",
                    InsuredMapper.KEY_RELATIONSHIP to "Father"
            ),
            PolicyOwnerMapper.KEY_BENEFICIARY to listOf(
                    mapOf(
                            BeneficiariesMapper.KEY_REFERENCE to "reference",
                            BeneficiariesMapper.KEY_DISPLAY to "display",
                            BeneficiariesMapper.KEY_RELATIONSHIP to "Father"
                    )
            ),
            PolicyOwnerMapper.KEY_INSURER to mapOf(
                    InsurerMapper.KEY_INSURER_NAME to "Malayan",
                    InsurerMapper.KEY_REFERENCE_INSURER to "reference",
                    InsurerMapper.KEY_CERTIFICATE_NUMBER to "123",
                    InsurerMapper.KEY_UNIT to "KHR",
                    InsurerMapper.KEY_PLAN to listOf(
                            mapOf(
                                    PlanMapper.KEY_IDENTIFIER to "7Y3OhYkpCx",
                                    PlanMapper.KEY_TIER to "Health & Personal Accident Plan",
                                    PlanMapper.KEY_RATE to 98568.00,
                                    PlanMapper.KEY_CATEGORY to "adult",
                                    PlanMapper.KEY_BENEFITS to listOf(mapOf(
                                            BenefitMapper.KEY_CATEGORY to "Hospitalization without major surgery (Public health facilities)",
                                            BenefitMapper.KEY_AMOUNT to 0.0,
                                            BenefitMapper.KEY_IDENTIFIER to "HwOmS_PHF"
                                    ))
                            )
                    ),
                    InsurerMapper.KEY_ATTACHMENT_INSURER to listOf("category1"),
                    InsurerMapper.KEY_QUALIFICATION to mapOf(
                            QualificationMapper.KEY_DATA to mapOf("a" to "b"),
                            QualificationMapper.KEY_REVIEWED_DATE to "1970-01-01T00:00:00Z",
                            QualificationMapper.KEY_DENIAL_REASON to "reason",
                            QualificationMapper.KEY_REVIEWED_BY to "someone",
                            QualificationMapper.KEY_IS_ACCEPTED to true
                    )
            ),
            PolicyOwnerMapper.KEY_STATUS to "status",
            PolicyOwnerMapper.KEY_APPLICATION to "1970-01-01T00:00:00Z",
            PolicyOwnerMapper.KEY_EFFECTIVE_DATE to "1970-01-01T00:00:00Z",
            PolicyOwnerMapper.KEY_EXPIRY to "1970-01-01T00:00:00Z",
            PolicyOwnerMapper.KEY_LAST_UPDATED to "1970-01-01T00:00:00Z",
            PolicyOwnerMapper.KEY_UPDATED_BY to mapOf(
                    UpdatedByMapper.KEY_REFERENCE to "reference",
                    UpdatedByMapper.KEY_DISPLAY to "display"
            ),
            PolicyOwnerMapper.KEY_PAST_STATUS to listOf(mapOf(
                    StatusMapper.KEY_STATUS to "active",
                    StatusMapper.KEY_STATUS_DATE to "1970-01-01T00:00:00Z",
                    StatusMapper.KEY_USER_DISPLAY_NAME to "joseph@gmail.com",
                    StatusMapper.KEY_USERNAME_ID to "1234",
                    StatusMapper.KEY_USERNAME to "joseph@gmail.com"
            ))
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(policyOwnerFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(policyOwnerFixture, mapper.unmarshal(mapFixture))
    }
}