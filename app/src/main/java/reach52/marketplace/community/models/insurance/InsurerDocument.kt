package reach52.marketplace.community.models.insurance

import org.threeten.bp.ZonedDateTime

data class InsurerDocument(
        val display: String,
        val identifier: String,
        val summary: String,
        val type: String,
        val organizationType: String,
        val contact: Contact,
        val formulary: List<Formulary>,
        val locale: Locale,
        val policy: Policy,
        val qualification: Qualification,
        val requirements: List<Requirement>,
        val updatedBy: UpdatedBy,
        val dateLastUpdated: ZonedDateTime
)
