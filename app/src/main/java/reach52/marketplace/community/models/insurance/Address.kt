package reach52.marketplace.community.models.insurance

data class Address (
        val city: String,
        val country: String,
        val line: List<String>,
        val state: String,
        val zip: String
)
