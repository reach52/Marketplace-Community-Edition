package reach52.marketplace.community.models

import org.threeten.bp.ZonedDateTime

data class InsuranceDomainResource(
        val identifier: String,
        val name: String,
        val code: String,
        val dateCreated: ZonedDateTime,
        val ownedBy: Company,
        val insurancePlan: List<InsurancePlan>,
        val coverage: List<CoverageInsurance>
)
