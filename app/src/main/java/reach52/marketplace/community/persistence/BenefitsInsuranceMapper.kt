package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.BenefitInsurance

class BenefitsInsuranceMapper : Unmarshaler<BenefitInsurance>, Marshaler<BenefitInsurance> {

    companion object {
        const val KEY_TYPE = "type"
        const val KEY_PLAN_REFERENCE = "planReference"
        const val KEY_REQUIREMENT = "requirement"

        private val requirements = RequirementsInsuranceMapper()
    }

    override fun marshal(t: BenefitInsurance): Map<String, Any> {
        return mapOf(
                KEY_TYPE to t.type,
                KEY_PLAN_REFERENCE to t.planReference,
                KEY_REQUIREMENT to t.requirement.map(requirements::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): BenefitInsurance {
        val type = properties[KEY_TYPE] as String
        val planReference = properties[KEY_PLAN_REFERENCE] as String
        val requirement = (properties[KEY_REQUIREMENT] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(requirements::unmarshal)

        return BenefitInsurance(
                type = type,
                planReference = planReference,
                requirement = requirement
        )

    }

}