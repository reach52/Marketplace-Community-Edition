package reach52.marketplace.community.models

data class BenefitInsurance(
        val type: String,
        val planReference: String,
        val requirement: List<Requirement>
)