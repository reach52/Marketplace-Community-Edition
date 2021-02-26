package reach52.marketplace.community.persistence.claims

import reach52.marketplace.community.models.claims.InsurancePolicies
import reach52.marketplace.community.persistence.claims_mapper.ClaimsInsurancePoliciesMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsInsurancePoliciesMapperTest {
    private val insurancePoliciesModelFixture = InsurancePolicies(
            insurancePolicyId = "insurancePolicyId",
            policyProvider = "policyProvider",
            policyPlan = "policyPlan",
            claimType = "claimType",
            beneficiary = "beneficiary",
            relationship = "relationship",
            contact = "contact",
            dateCreated = ZonedDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            dateExpiry = ZonedDateTime.parse("2021-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    )

    private val insurancePoliciesMapFixture = mapOf(
            ClaimsInsurancePoliciesMapper.KEY_INSURANCE_POLICY_ID to "insurancePolicyId",
            ClaimsInsurancePoliciesMapper.KEY_INSURANCE_POLICY_PROVIDER to "policyProvider",
            ClaimsInsurancePoliciesMapper.KEY_POLICY_PLAN to "policyPlan",
            ClaimsInsurancePoliciesMapper.KEY_CLAIM_TYPE to "claimType",
            ClaimsInsurancePoliciesMapper.KEY_BENEFICIARY to "beneficiary",
            ClaimsInsurancePoliciesMapper.KEY_RELATIONSHIP to "relationship",
            ClaimsInsurancePoliciesMapper.KEY_CONTACT to "contact",
            ClaimsInsurancePoliciesMapper.KEY_DATE_CREATED to "2020-01-01T00:00:00Z",
            ClaimsInsurancePoliciesMapper.KEY_DATE_EXPIRY to "2021-01-01T00:00:00Z"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(insurancePoliciesModelFixture, ClaimsInsurancePoliciesMapper().unmarshal(insurancePoliciesMapFixture))
    }

    @Test
    fun marshal() {
        Assert.assertEquals(insurancePoliciesMapFixture, ClaimsInsurancePoliciesMapper().marshal(insurancePoliciesModelFixture))
    }
}
