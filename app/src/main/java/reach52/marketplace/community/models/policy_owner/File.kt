package reach52.marketplace.community.models.policy_owner

data class File (
        val contentType: String,
        val digest: String,
        val length: Double,
        val repos: Int,
        val stub: Boolean
)
