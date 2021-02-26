package reach52.marketplace.community.models.insurance

data class Formulary (
        val beneficiary: Int,
        val category: String,
        val benefits: List<Benefit>,
        val identifier: String,
        val rate: Double,
        val summary: String,
        val tier: String,
        val ageRange: AgeRange
)