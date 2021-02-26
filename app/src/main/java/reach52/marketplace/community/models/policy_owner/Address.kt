package reach52.marketplace.community.models.policy_owner

data class Address (
        val city: String,
        val country: String,
        val line: List<String>,
        val state: String,
        val zip: String
)
