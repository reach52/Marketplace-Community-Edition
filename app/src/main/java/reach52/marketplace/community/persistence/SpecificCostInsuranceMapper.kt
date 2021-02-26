package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.SpecificCost

class SpecificCostInsuranceMapper : Unmarshaler<SpecificCost>, Marshaler<SpecificCost> {

    companion object {
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_CATEGORY = "category"
        const val KEY_COST = "cost"
    }

    override fun marshal(t: SpecificCost): Map<String, Any> {
        return mapOf(
                KEY_IDENTIFIER to t.identifier,
                KEY_CATEGORY to t.category,
                KEY_COST to t.cost
        )
    }

    override fun unmarshal(properties: Map<String, Any>): SpecificCost {
        val identifier = properties[KEY_IDENTIFIER] as String
        val category = properties[KEY_CATEGORY] as String
        val cost = (properties[KEY_COST] as Number).toDouble()

        return SpecificCost(
                identifier = identifier,
                category = category,
                cost = cost
        )
    }

}