package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.CoverageInsured


class CoverageInsuredMapper : Marshaler<CoverageInsured>, Unmarshaler<CoverageInsured> {
    companion object {
        const val KEY_COVERAGE_BENEFICIARY = "beneficiary"
        const val KEY_COVERAGE_DEPENDENT = "dependent"
        const val KEY_COVERAGE_RELATIONSHIP = "relationship"
    }

    override fun marshal(t: CoverageInsured): Map<String, Any> {
        return mapOf(
                KEY_COVERAGE_BENEFICIARY to t.beneficiary,
                KEY_COVERAGE_DEPENDENT to t.dependent,
                KEY_COVERAGE_RELATIONSHIP to t.relationship
        )
    }


    override fun unmarshal(properties: Map<String, Any>): CoverageInsured {
        val beneficiary = properties[KEY_COVERAGE_BENEFICIARY] as String
        val dependent = properties[KEY_COVERAGE_DEPENDENT] as String
        val relationship = properties[KEY_COVERAGE_RELATIONSHIP] as String

        return CoverageInsured(
                beneficiary = beneficiary,
                dependent = dependent,
                relationship = relationship
        )
    }
}