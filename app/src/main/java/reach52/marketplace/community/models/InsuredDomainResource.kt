package reach52.marketplace.community.models

import reach52.marketplace.community.models.insured.Address
import org.threeten.bp.ZonedDateTime

data class InsuredDomainResource(
        val identifier: String,
        val resourceType: String,
        val address: Address,
        val dateOfBirth: ZonedDateTime,
        val civilStatus: String,
        val gender: String,
        val contact: String,
        val subject: Subject,
        val period: CoveragePeriod,
        val plan: List<PlanReference>,
        val coverage: List<CoverageInsured>,
        val emailAddress: String
)