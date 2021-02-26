package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.PlanReference
import reach52.marketplace.community.models.SpecificCost
import reach52.marketplace.community.models.Status

class PlanReferenceMapper : Marshaler<PlanReference>, Unmarshaler<PlanReference> {
    companion object {
        const val KEY_PLAN_TITLE = "title"
        const val KEY_PLAN_PRICE = "price"
        const val KEY_PLAN_REFERENCE = "planReference"
        const val KEY_PLAN_OWNER = "planOwner"
        const val KEY_PLAN_OWNER_NAME = "planOwnerName"
        const val KEY_PLAN_CURRENT_STATUS = "currentStatus"
        const val KEY_PLAN_PAST_STATUSES = "pastStatuses"
        const val KEY_PLAN_SPECIFIC_COST = "specificCost"
        const val KEY_PLAN_EFFECTIVE_DATE = "effectiveDate"

        val statusMapper = InsuredStatusMapper()
        val planSpecificCostMapper = SpecificCostMapper()
        val planEffectiveDateMapper = EffectiveDateMapper()
    }

    override fun marshal(t: PlanReference): Map<String, Any> {
        return mapOf(
                KEY_PLAN_TITLE to t.title,
                KEY_PLAN_PRICE to t.price,
                KEY_PLAN_REFERENCE to t.planReference,
                KEY_PLAN_OWNER to t.planOwner,
                KEY_PLAN_OWNER_NAME to t.planOwnerName,
                KEY_PLAN_CURRENT_STATUS to statusMapper.marshal(t.currentStatus),
                KEY_PLAN_PAST_STATUSES to t.pastStatuses.map(statusMapper::marshal),
                KEY_PLAN_SPECIFIC_COST to t.specificCost.map(planSpecificCostMapper::marshal),
                KEY_PLAN_EFFECTIVE_DATE to planEffectiveDateMapper.marshal(t.effectiveDate)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): PlanReference {
        val title = properties[KEY_PLAN_TITLE] as String
        val price = properties[KEY_PLAN_PRICE] as Double
        val planReference = properties[KEY_PLAN_REFERENCE] as String
        val planOwner = properties[KEY_PLAN_OWNER] as String
        val planOwnerName = properties[KEY_PLAN_OWNER_NAME] as String

        @Suppress("UNCHECKED_CAST")
        val currentStatusMap = (properties[KEY_PLAN_CURRENT_STATUS] as Map<String, Any>)
        val currentStatus = InsuredStatusMapper().unmarshal(currentStatusMap)

        @Suppress("UNCHECKED_CAST")
        val pastStatuses = (properties[KEY_PLAN_PAST_STATUSES] as List<Status>)
                .filterIsInstance<Map<String, Any>>()
                .map(statusMapper::unmarshal)

        @Suppress("UNCHECKED_CAST")
        val specificCost = (properties[KEY_PLAN_SPECIFIC_COST] as List<SpecificCost>)
                .filterIsInstance<Map<String, Any>>()
                .map(planSpecificCostMapper::unmarshal)

        @Suppress("UNCHECKED_CAST")
        val effectiveDateMap = (properties[KEY_PLAN_EFFECTIVE_DATE] as Map<String, Any>)
        val effectiveDate = EffectiveDateMapper().unmarshal(effectiveDateMap)


        return PlanReference(
                title = title,
                price = price,
                planReference = planReference,
                planOwner = planOwner,
                planOwnerName = planOwnerName,
                currentStatus = currentStatus,
                pastStatuses = pastStatuses,
                specificCost = specificCost,
                effectiveDate = effectiveDate
        )
    }
}