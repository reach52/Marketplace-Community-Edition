package reach52.marketplace.community.models.insurance

data class Requirement (
        val category: String,
        val identifier: String,
        val option: Boolean,
        val references: List<String>,
        val variations: List<String>
)
