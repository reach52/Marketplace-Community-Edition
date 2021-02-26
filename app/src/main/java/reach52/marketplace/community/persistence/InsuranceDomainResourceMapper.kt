package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.InsuranceDomainResource
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsuranceDomainResourceMapper : Marshaler<InsuranceDomainResource>, Unmarshaler<InsuranceDomainResource> {

    companion object {
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_NAME = "name"
        const val KEY_CODE = "code"
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_OWNED_BY = "ownedBy"
        const val KEY_PLAN = "plan"
        const val KEY_COVERAGE = "coverage"

        private val companyMapper = CompanyInsuranceMapper()
        private val planMapper = InsurancePlanMapper()
        private val coverageInsuranceMapper = CoverageInsuranceMapper()

    }

    override fun marshal(t: InsuranceDomainResource): Map<String, Any> {
        return mapOf(
                KEY_IDENTIFIER to t.identifier,
                KEY_NAME to t.name,
                KEY_CODE to t.code,
                KEY_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_OWNED_BY to companyMapper.marshal(t.ownedBy),
                KEY_PLAN to t.insurancePlan.map(planMapper::marshal),
                KEY_COVERAGE to t.coverage.map(coverageInsuranceMapper::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): InsuranceDomainResource {
        val identifier = properties[KEY_IDENTIFIER] as String
        val name = properties[KEY_NAME] as String
        val code = properties[KEY_CODE] as String
        val dateCreated = ZonedDateTime.parse(properties[KEY_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        @Suppress("UNCHECKED_CAST")
        val ownedBy = companyMapper.unmarshal(properties[KEY_OWNED_BY] as Map<String, Any>)

        val plan = (properties[KEY_PLAN] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(planMapper::unmarshal)

        val coverage = (properties[KEY_COVERAGE] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(coverageInsuranceMapper::unmarshal)

        return InsuranceDomainResource(
                identifier = identifier,
                name = name,
                code = code,
                dateCreated = dateCreated,
                ownedBy = ownedBy,
                insurancePlan = plan,
                coverage = coverage
        )
    }
}