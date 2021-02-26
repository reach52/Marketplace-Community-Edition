package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.InsurancePolicies
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsInsurancePoliciesMapper : Unmarshaler<InsurancePolicies>, Marshaler<InsurancePolicies> {
    companion object {
        const val KEY_INSURANCE_POLICY_ID = "insurancePolicyId"
        const val KEY_INSURANCE_POLICY_PROVIDER = "policyProvider"
        const val KEY_POLICY_PLAN = "policyPlan"
        const val KEY_CLAIM_TYPE = "claimType"
        const val KEY_BENEFICIARY = "beneficiary"
        const val KEY_RELATIONSHIP = "relationship"
        const val KEY_CONTACT = "contact"
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_DATE_EXPIRY = "dateExpiry"
    }

    override fun marshal(t: InsurancePolicies): Map<String, Any> {
        return mapOf(
                KEY_INSURANCE_POLICY_ID to t.insurancePolicyId,
                KEY_INSURANCE_POLICY_PROVIDER to t.policyProvider,
                KEY_POLICY_PLAN to t.policyPlan,
                KEY_CLAIM_TYPE to t.claimType,
                KEY_BENEFICIARY to t.beneficiary,
                KEY_RELATIONSHIP to t.relationship,
                KEY_CONTACT to t.contact,
                KEY_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_DATE_EXPIRY to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateExpiry)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): InsurancePolicies {
        val insurancePolicyId = properties[KEY_INSURANCE_POLICY_ID] as String
        val policyProvider = properties[KEY_INSURANCE_POLICY_PROVIDER] as String
        val policyPlan = properties[KEY_POLICY_PLAN] as String
        val claimType = properties[KEY_CLAIM_TYPE] as String
        val beneficiary = properties[KEY_BENEFICIARY] as String
        val relationship = properties[KEY_RELATIONSHIP] as String
        val contact = properties[KEY_CONTACT] as String
        val dateCreated = ZonedDateTime.parse(properties[KEY_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val dateExpiry = ZonedDateTime.parse(properties[KEY_DATE_EXPIRY] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        return InsurancePolicies(
                insurancePolicyId = insurancePolicyId,
                policyProvider = policyProvider,
                policyPlan = policyPlan,
                claimType = claimType,
                beneficiary = beneficiary,
                relationship = relationship,
                contact = contact,
                dateCreated = dateCreated,
                dateExpiry = dateExpiry
        )
    }
}