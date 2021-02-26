package reach52.marketplace.community.persistence.claims

import arrow.core.None
import reach52.marketplace.community.models.claims.*
import reach52.marketplace.community.persistence.claims_mapper.*
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsMapperTest {
    private val claimsModelFixture = Claims(
            identifier = "identifier",
            dateCreated = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            source = "source",
            createdBy = CreatedBy(
                    identifier = "identifier",
                    username = "username",
                    role = "role",
                    email = "email"
            ),
            type = "type",
            dateOfEvent = ZonedDateTime.parse("2019-02-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            claimID = "claimID",
            customer = Customer(
                    customerID = "customerID",
                    firstName = "firstName",
                    middleName = "middleName",
                    lastName = "lastName",
                    gender = "gender",
                    age = 20,
                    email = None,
                    address = "address",
                    contact = None,
                    maritalStatus = "maritalStatus"
            ),
            insurancePolicies = InsurancePolicies(
                    insurancePolicyId = "insurancePolicyId",
                    policyProvider = "policyProvider",
                    policyPlan = "policyPlan",
                    claimType = "claimType",
                    beneficiary = "beneficiary",
                    relationship = "relationship",
                    contact = "contact",
                    dateCreated = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    dateExpiry = ZonedDateTime.parse("2021-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            ),
            benefits = Benefits(
                    claimAmount = 60.000,
                    notifiedDate = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            ),
            benefitsClaim = BenefitClaims(
                    claimAmount = 60.000,
                    claimNotifiedDate = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    lastClaimSubmittedDate = ZonedDateTime.parse("2020-01-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    userID = "userID"
            ),
            currentStatus = CurrentStatus(
                    status = "status",
                    username = "username",
                    userID = "userID",
                    dateOfStatus = ZonedDateTime.parse("2020-02-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            ),
            pastStatuses = PastStatuses(
                    status = "status",
                    username = "username",
                    userID = "userID",
                    dateOfStatus = ZonedDateTime.parse("2020-01-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            ),
            submittedBasicRequirements = SubmittedBasicRequirements(
                    code = "code",
                    type = "type",
                    dateSubmitted = ZonedDateTime.parse("2020-01-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    attachmentName = "attachmentName"
            ),
            submittedRequirement = SubmittedRequirements(
                    code = "code",
                    type = "type",
                    dateSubmitted = ZonedDateTime.parse("2020-02-10T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    attachmentName = "attachmentName"
            )
    )

    private val claimsMapFixture = mapOf(
            ClaimsMapper.KEY_CLAIMS_IDENTIFIER to "identifier",
            ClaimsMapper.KEY_CLAIMS_DATE_CREATED to "2020-01-01T00:00:00Z",
            ClaimsMapper.KEY_CLAIMS_SOURCE to "source",
            ClaimsMapper.KEY_CLAIMS_CREATED_BY to mapOf(
                    ClaimsCreatedByMapper.KEY_CREATED_BY_ID to "identifier",
                    ClaimsCreatedByMapper.KEY_CREATED_BY_USERNAME to "username",
                    ClaimsCreatedByMapper.KEY_CREATED_BY_ROLE to "role",
                    ClaimsCreatedByMapper.KEY_CREATED_BY_EMAIL to "email"
            ),
            ClaimsMapper.KEY_CLAIMS_TYPE to "type",
            ClaimsMapper.KEY_CLAIMS_DATE_OF_EVENT to "2019-02-01T00:00:00Z",
            ClaimsMapper.KEY_CLAIMS_CLAIM_ID to "claimID",
            ClaimsMapper.KEY_CLAIMS_CUSTOMER to mapOf(
                    CustomerMapper.KEY_CUSTOMER_ID to "customerID",
                    CustomerMapper.KEY_CUSTOMER_FIRST_NAME to "firstName",
                    CustomerMapper.KEY_CUSTOMER_MIDDLE_NAME to "middleName",
                    CustomerMapper.KEY_CUSTOMER_LAST_NAME to "lastName",
                    CustomerMapper.KEY_CUSTOMER_GENDER to "gender",
                    CustomerMapper.KEY_CUSTOMER_AGE to 20,
                    CustomerMapper.KEY_CUSTOMER_ADDRESS to "address",
                    CustomerMapper.KEY_CUSTOMER_MARITAL_STATUS to "maritalStatus"
            ),
            ClaimsMapper.KEY_CLAIMS_INSURANCE_POLICIES to mapOf(
                    ClaimsInsurancePoliciesMapper.KEY_INSURANCE_POLICY_ID to "insurancePolicyId",
                    ClaimsInsurancePoliciesMapper.KEY_INSURANCE_POLICY_PROVIDER to "policyProvider",
                    ClaimsInsurancePoliciesMapper.KEY_POLICY_PLAN to "policyPlan",
                    ClaimsInsurancePoliciesMapper.KEY_CLAIM_TYPE to "claimType",
                    ClaimsInsurancePoliciesMapper.KEY_BENEFICIARY to "beneficiary",
                    ClaimsInsurancePoliciesMapper.KEY_RELATIONSHIP to "relationship",
                    ClaimsInsurancePoliciesMapper.KEY_CONTACT to "contact",
                    ClaimsInsurancePoliciesMapper.KEY_DATE_CREATED to "2020-01-01T00:00:00Z",
                    ClaimsInsurancePoliciesMapper.KEY_DATE_EXPIRY to "2021-01-01T00:00:00Z"
            ),
            ClaimsMapper.KEY_CLAIMS_BENEFITS to mapOf(
                    BenefitsMapper.KEY_BENEFITS_CLAIM_AMOUNT to 60.00,
                    BenefitsMapper.KEY_BENEFITS_NOTIFIED_DATE to "2020-01-01T00:00:00Z"
            ),
            ClaimsMapper.KEY_CLAIMS_BENEFITS_CLAIMS to mapOf(
                    BenefitClaimsMapper.KEY_CLAIM_AMOUNT to 60.00,
                    BenefitClaimsMapper.KEY_CLAIM_NOTIFIED_DATE to "2020-01-01T00:00:00Z",
                    BenefitClaimsMapper.KEY_LAST_CLAIM_SUBMITTED_DATE to "2020-01-10T00:00:00Z",
                    BenefitClaimsMapper.KEY_USER_ID to "userID"
            ),
            ClaimsMapper.KEY_CLAIMS_CURRENT_STATUS to mapOf(
                    ClaimsCurrentStatusMapper.KEY_STATUS to "status",
                    ClaimsCurrentStatusMapper.KEY_CURRENT_STATUS_USERNAME to "username",
                    ClaimsCurrentStatusMapper.KEY_CURRENT_STATUS_USER_ID to "userID",
                    ClaimsCurrentStatusMapper.KEY_CURRENT_DATE_OF_STATUS to "2020-02-05T00:00:00Z"
            ),
            ClaimsMapper.KEY_CLAIMS_PAST_STATUSES to mapOf(
                    ClaimsPastStatusesMapper.KEY_PAST_STATUS to "status",
                    ClaimsPastStatusesMapper.KEY_PAST_STATUS_USERNAME to "username",
                    ClaimsPastStatusesMapper.KEY_PAST_STATUS_USER_ID to "userID",
                    ClaimsPastStatusesMapper.KEY_PAST_STATUS_DATE_OF_STATUS to "2020-01-10T00:00:00Z"
            ),
            ClaimsMapper.KEY_CLAIMS_SUBMITTED_BASIC_REQUIREMENTS to mapOf(
                    SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_CODE to "code",
                    SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_TYPE to "type",
                    SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_DATE_SUBMITTED to "2020-01-10T00:00:00Z",
                    SubmittedBasicRequirementsMapper.KEY_BASIC_REQ_ATTACHMENT_NAME to "attachmentName"
            ),
            ClaimsMapper.KEY_CLAIMS_SUBMITTED_REQUIREMENTS to mapOf(
                    SubmittedRequirementsMapper.KEY_REQUIREMENTS_CODE to "code",
                    SubmittedRequirementsMapper.KEY_REQUIREMENTS_TYPE to "type",
                    SubmittedRequirementsMapper.KEY_REQUIREMENTS_DATE_SUBMITTED to "2020-02-10T00:00:00Z",
                    SubmittedRequirementsMapper.KEY_REQUIREMENTS_ATTACHMENT_NAME to "attachmentName"
            )
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(claimsModelFixture, ClaimsMapper().unmarshal(claimsMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(claimsMapFixture, ClaimsMapper().marshal(claimsModelFixture))
    }
}