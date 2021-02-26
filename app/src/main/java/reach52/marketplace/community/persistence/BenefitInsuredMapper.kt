package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.BenefitInsured

class BenefitInsuredMapper : Marshaler<BenefitInsured>, Unmarshaler<BenefitInsured> {
    companion object {
        const val KEY_BENEFIT_TYPE = "type"
        const val KEY_BENEFIT_PLAN_REFERENCE = "planReference"
        const val KEY_BENEFIT_REQUIREMENT = "requirement"

        val requirement = RequirementReferenceMapper()
    }

    override fun marshal(t: BenefitInsured): Map<String, Any> {
        return mapOf(
                KEY_BENEFIT_TYPE to t.type,
                KEY_BENEFIT_PLAN_REFERENCE to t.planReference,
                KEY_BENEFIT_REQUIREMENT to t.requirement.map(requirement::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): BenefitInsured {
        val type = properties[KEY_BENEFIT_TYPE] as String
        val planReference = properties[KEY_BENEFIT_PLAN_REFERENCE] as String
        val requirementReference = (properties[KEY_BENEFIT_REQUIREMENT] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(requirement::unmarshal)


        return BenefitInsured(
                type = type,
                planReference = planReference,
                requirement = requirementReference
        )
    }
}