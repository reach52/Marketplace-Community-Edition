package reach52.marketplace.community.models.claims

import org.threeten.bp.ZonedDateTime

data class Claims(
        val identifier: String,
        val dateCreated: ZonedDateTime,
        val source: String,
        val createdBy: CreatedBy,
        val type: String,
        val dateOfEvent: ZonedDateTime,
        val claimID: String,
        val customer: Customer,
        val insurancePolicies: InsurancePolicies,
        val benefits: Benefits,
        val benefitsClaim: BenefitClaims,
        val currentStatus: CurrentStatus,
        val pastStatuses: PastStatuses,
        val submittedBasicRequirements: SubmittedBasicRequirements,
        val submittedRequirement: SubmittedRequirements
)