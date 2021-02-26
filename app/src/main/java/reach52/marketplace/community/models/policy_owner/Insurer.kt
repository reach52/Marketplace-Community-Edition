package reach52.marketplace.community.models.policy_owner

data class Insurer(
        val insurerName: String,
        val reference: String,
        val certificateNumber: String,
        val unit: String,
        val plan: List<Plan>,
        val attachments: List<String>,
        val qualification: Qualification
)