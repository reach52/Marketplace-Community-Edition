package reach52.marketplace.community.models

data class PlanReference(
        val title: String,
        val price: Double,
        val planReference: String,
        val planOwner: String,
        val planOwnerName: String,
        val currentStatus: Status,
        val pastStatuses: List<Status>,
        val specificCost: List<SpecificCost>,
        val effectiveDate: EffectiveDate
)
