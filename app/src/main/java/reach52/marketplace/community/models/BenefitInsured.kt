package reach52.marketplace.community.models

data class BenefitInsured(
        val type: String,
        val planReference: String,
        val requirement: List<RequirementReference>
)