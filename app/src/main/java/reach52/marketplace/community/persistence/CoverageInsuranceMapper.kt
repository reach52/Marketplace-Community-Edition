package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.CoverageInsurance

class CoverageInsuranceMapper : Unmarshaler<CoverageInsurance>, Marshaler<CoverageInsurance> {

    companion object {
        const val KEY_BENEFITS = "benefit"

        private val benefitsInsuranceMapper = BenefitsInsuranceMapper()
    }

    override fun marshal(t: CoverageInsurance): Map<String, Any> {
        return mapOf(
                KEY_BENEFITS to t.benefit.map(benefitsInsuranceMapper::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): CoverageInsurance {
        val benefits = (properties[KEY_BENEFITS] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(benefitsInsuranceMapper::unmarshal)

        return CoverageInsurance(
                benefit = benefits
        )
    }
}