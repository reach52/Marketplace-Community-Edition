package reach52.marketplace.community.models.insurance

data class Locale (
        val identifier: String,
        val languages: List<String>,
        val pricing: Pricing
)