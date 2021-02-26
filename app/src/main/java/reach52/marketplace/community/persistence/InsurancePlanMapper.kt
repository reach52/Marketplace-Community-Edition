package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.InsurancePlan

class InsurancePlanMapper : Unmarshaler<InsurancePlan>, Marshaler<InsurancePlan> {

    companion object {
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_TITLE = "title"
        const val KEY_CODE = "code"
        const val KEY_DETAILS = "details"
        const val KEY_SPECIFIC_COST = "specificCost"
        const val KEY_PRICE = "price"
        const val KEY_DATE_EXPIRY = "dateExpiry"

        private val specificCostMapper = SpecificCostInsuranceMapper()
    }

    override fun marshal(t: InsurancePlan): Map<String, Any> {
        return mapOf(
                KEY_IDENTIFIER to t.identifier,
                KEY_TITLE to t.title,
                KEY_CODE to t.code,
                KEY_PRICE to t.price,
                KEY_DATE_EXPIRY to t.dateExpiry,
                KEY_DETAILS to t.details,
                KEY_SPECIFIC_COST to t.specificCosts.map(specificCostMapper::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): InsurancePlan {
        val identifier = properties[KEY_IDENTIFIER] as String
        val title = properties[KEY_TITLE] as String
        val code = properties[KEY_CODE] as String
        val details = properties[KEY_DETAILS] as String
        val expiryDate = (properties[KEY_DATE_EXPIRY] as Number).toLong()
        val price = (properties[KEY_PRICE] as Number).toDouble()
        val specificCost = (properties[KEY_SPECIFIC_COST] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(specificCostMapper::unmarshal)

        return InsurancePlan(
                identifier = identifier,
                title = title,
                code = code,
                details = details,
                specificCosts = specificCost,
                price = price,
                dateExpiry = expiryDate
        )
    }
}