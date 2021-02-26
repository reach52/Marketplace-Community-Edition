package reach52.marketplace.community.persistence

import arrow.core.Option
import arrow.core.getOrElse
import reach52.marketplace.community.models.CoverageInsured
import reach52.marketplace.community.models.InsuredDomainResource
import reach52.marketplace.community.models.PlanReference
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class InsuredDomainResourceMapper : Marshaler<InsuredDomainResource>, Unmarshaler<InsuredDomainResource> {
    companion object {
        const val KEY_INSURED_ID = "identifier"
        const val KEY_RESOURCE_TYPE = "resourceType"
        const val KEY_INSURED_ADDRESS = "address"
        const val KEY_INSURED_DATE_OF_BIRTH = "dateOfBirth"
        const val KEY_INSURED_CIVIL_STATUS = "civilStatus"
        const val KEY_INSURED_GENDER = "gender"
        const val KEY_INSURED_CONTACT = "contact"
        const val KEY_INSURED_SUBJECT = "subject"
        const val KEY_INSURED_PLAN = "plan"
        const val KEY_INSURED_COVERAGE_PLAN = "coverage"
        const val KEY_COVERAGE_PERIOD = "period"
        const val KEY_EMAIL = "email"

        val insuredPlanReferenceMapper = PlanReferenceMapper()
        val insuredCoverageMapper = CoverageInsuredMapper()
        val insuredAddress = AddressMapper()
        val insuredSubject = SubjectMapper()
        val coveragePeriodMapper = CoveragePeriodMapper()
    }

    override fun marshal(t: InsuredDomainResource): Map<String, Any> {
        return mapOf(
                KEY_INSURED_ID to t.identifier,
                KEY_RESOURCE_TYPE to t.resourceType,
                KEY_INSURED_ADDRESS to insuredAddress.marshal(t.address),
                KEY_INSURED_DATE_OF_BIRTH to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateOfBirth),
                KEY_INSURED_CIVIL_STATUS to t.civilStatus,
                KEY_INSURED_GENDER to t.gender,
                KEY_INSURED_CONTACT to t.contact,
                KEY_INSURED_SUBJECT to insuredSubject.marshal(t.subject),
                KEY_COVERAGE_PERIOD to coveragePeriodMapper.marshal(t.period),
                KEY_INSURED_PLAN to t.plan.map(insuredPlanReferenceMapper::marshal),
                KEY_INSURED_COVERAGE_PLAN to t.coverage.map(insuredCoverageMapper::marshal),
                KEY_EMAIL to t.emailAddress
        )
    }

    override fun unmarshal(properties: Map<String, Any>): InsuredDomainResource {
        @Suppress("UNCHECKED_CAST")
        val identifier = properties[KEY_INSURED_ID] as String
        val resourceType = properties[KEY_RESOURCE_TYPE] as String

        @Suppress("UNCHECKED_CAST")
        val addressMap = (properties[KEY_INSURED_ADDRESS] as Map<String, Any>)

        val address = AddressMapper().unmarshal(addressMap)

        val dateOfBirth = ZonedDateTime.parse(properties[KEY_INSURED_DATE_OF_BIRTH] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val civilStatus = properties[KEY_INSURED_CIVIL_STATUS] as String
        val gender = properties[KEY_INSURED_GENDER] as String


        @Suppress("UNCHECKED_CAST")
        val subjectMap = properties[KEY_INSURED_SUBJECT] as Map<String, Any>
        val subject = SubjectMapper().unmarshal(subjectMap)

        @Suppress("UNCHECKED_CAST")
        val plan = (properties[KEY_INSURED_PLAN] as List<PlanReference>)
                .filterIsInstance<Map<String, Any>>()
                .map(insuredPlanReferenceMapper::unmarshal)

        @Suppress("UNCHECKED_CAST")
        val coverage = (properties[KEY_INSURED_COVERAGE_PLAN] as List<CoverageInsured>)
                .filterIsInstance<Map<String, Any>>()
                .map(insuredCoverageMapper::unmarshal)

        @Suppress("UNCHECKED_CAST")
        val coveragePeriodMap = properties[KEY_COVERAGE_PERIOD] as Map<String, Any>

        val emailAddress = Option.fromNullable(properties[KEY_EMAIL] as? String)
        val contact = Option.fromNullable(properties[KEY_INSURED_CONTACT] as? String)

        return InsuredDomainResource(
                identifier = identifier,
                resourceType = resourceType,
                address = address,
                civilStatus = civilStatus,
                dateOfBirth = dateOfBirth,
                gender = gender,
                contact = contact.getOrElse { "none" },
                period = coveragePeriodMapper.unmarshal(coveragePeriodMap),
                subject = subject,
                plan = plan,
                coverage = coverage,
                emailAddress = emailAddress.getOrElse { "none" }
        )
    }
}