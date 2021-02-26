package reach52.marketplace.community.models.claims

import org.threeten.bp.ZonedDateTime

data class InsurancePolicies(
        val insurancePolicyId: String,
        val policyProvider: String,
        val policyPlan: String,
        val claimType: String,
        val beneficiary: String,
        val relationship: String,
        val contact: String,
        val dateCreated: ZonedDateTime,
        val dateExpiry: ZonedDateTime
)