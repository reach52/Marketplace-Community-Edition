package reach52.marketplace.community.models

data class InsurancePlan(
        val identifier: String,
        val title: String,
        val code: String,
        val details: String,
        val price: Double,
        val dateExpiry: Long,
        val specificCosts: List<SpecificCost>
)