package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.Claims
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ClaimsMapper : Marshaler<Claims>, Unmarshaler<Claims> {
    companion object {
        const val KEY_CLAIMS_IDENTIFIER = "identifier"
        const val KEY_CLAIMS_DATE_CREATED = "dateCreated"
        const val KEY_CLAIMS_SOURCE = "source"
        const val KEY_CLAIMS_CREATED_BY = "createdBy"
        const val KEY_CLAIMS_TYPE = "type"
        const val KEY_CLAIMS_DATE_OF_EVENT = "dateOfEvent"
        const val KEY_CLAIMS_CLAIM_ID = "claimID"
        const val KEY_CLAIMS_CUSTOMER = "customer"
        const val KEY_CLAIMS_INSURANCE_POLICIES = "insurancePolicies"
        const val KEY_CLAIMS_BENEFITS = "benefits"
        const val KEY_CLAIMS_BENEFITS_CLAIMS = "benefitsClaims"
        const val KEY_CLAIMS_CURRENT_STATUS = "currentStatus"
        const val KEY_CLAIMS_PAST_STATUSES = "pastStatuses"
        const val KEY_CLAIMS_SUBMITTED_BASIC_REQUIREMENTS = "submittedBasicRequirements"
        const val KEY_CLAIMS_SUBMITTED_REQUIREMENTS = "submittedRequirements"

        val claimsCreatedBy = ClaimsCreatedByMapper()
        val claimsCustomer = CustomerMapper()
        val claimsInsurancePolicies = ClaimsInsurancePoliciesMapper()
        val claimsBenefits = BenefitsMapper()
        val claimsBenefitsClaims = BenefitClaimsMapper()
        val claimsCurrentStatus = ClaimsCurrentStatusMapper()
        val claimsPastStatuses = ClaimsPastStatusesMapper()
        val claimsBasicRequirements = SubmittedBasicRequirementsMapper()
        val claimsSubmittedRequirements = SubmittedRequirementsMapper()
    }

    override fun marshal(t: Claims): Map<String, Any> {
        return mapOf(
                KEY_CLAIMS_IDENTIFIER to t.identifier,
                KEY_CLAIMS_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_CLAIMS_SOURCE to t.source,
                KEY_CLAIMS_CREATED_BY to claimsCreatedBy.marshal(t.createdBy),
                KEY_CLAIMS_TYPE to t.type,
                KEY_CLAIMS_DATE_OF_EVENT to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateOfEvent),
                KEY_CLAIMS_CLAIM_ID to t.claimID,
                KEY_CLAIMS_CUSTOMER to claimsCustomer.marshal(t.customer),
                KEY_CLAIMS_INSURANCE_POLICIES to claimsInsurancePolicies.marshal(t.insurancePolicies),
                KEY_CLAIMS_BENEFITS to claimsBenefits.marshal(t.benefits),
                KEY_CLAIMS_BENEFITS_CLAIMS to claimsBenefitsClaims.marshal(t.benefitsClaim),
                KEY_CLAIMS_CURRENT_STATUS to claimsCurrentStatus.marshal(t.currentStatus),
                KEY_CLAIMS_PAST_STATUSES to claimsPastStatuses.marshal(t.pastStatuses),
                KEY_CLAIMS_SUBMITTED_BASIC_REQUIREMENTS to claimsBasicRequirements.marshal(t.submittedBasicRequirements),
                KEY_CLAIMS_SUBMITTED_REQUIREMENTS to claimsSubmittedRequirements.marshal(t.submittedRequirement)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Claims {
        val identifier = properties[KEY_CLAIMS_IDENTIFIER] as String
        val dateCreated = ZonedDateTime.parse(properties[KEY_CLAIMS_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val source = properties[KEY_CLAIMS_SOURCE] as String

        @Suppress("UNCHECKED_CAST")
        val createdByMap = (properties[KEY_CLAIMS_CREATED_BY] as Map<String, Any>)
        val createdBy = ClaimsCreatedByMapper().unmarshal(createdByMap)

        val type = properties[KEY_CLAIMS_TYPE] as String
        val dateOfEvent = ZonedDateTime.parse(properties[KEY_CLAIMS_DATE_OF_EVENT] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val claimID = properties[KEY_CLAIMS_CLAIM_ID] as String

        @Suppress("UNCHECKED_CAST")
        val customerMap = (properties[KEY_CLAIMS_CUSTOMER] as Map<String, Any>)
        val customer = CustomerMapper().unmarshal(customerMap)

        @Suppress("UNCHECKED_CAST")
        val insurancePoliciesMap = (properties[KEY_CLAIMS_INSURANCE_POLICIES] as Map<String, Any>)
        val insurancePolicies = ClaimsInsurancePoliciesMapper().unmarshal(insurancePoliciesMap)

        @Suppress("UNCHECKED_CAST")
        val benefitsMap = (properties[KEY_CLAIMS_BENEFITS] as Map<String, Any>)
        val benefits = BenefitsMapper().unmarshal(benefitsMap)

        @Suppress("UNCHECKED_CAST")
        val benefitsClaimsMap = (properties[KEY_CLAIMS_BENEFITS_CLAIMS] as Map<String, Any>)
        val benefitsClaims = BenefitClaimsMapper().unmarshal(benefitsClaimsMap)

        @Suppress("UNCHECKED_CAST")
        val currentStatusMap = (properties[KEY_CLAIMS_CURRENT_STATUS] as Map<String, Any>)
        val currentStatus = ClaimsCurrentStatusMapper().unmarshal(currentStatusMap)

        @Suppress("UNCHECKED_CAST")
        val pastStatusesMap = (properties[KEY_CLAIMS_PAST_STATUSES] as Map<String, Any>)
        val pastStatuses = ClaimsPastStatusesMapper().unmarshal(pastStatusesMap)

        @Suppress("UNCHECKED_CAST")
        val submittedBasicRequirementsMap = (properties[KEY_CLAIMS_SUBMITTED_BASIC_REQUIREMENTS] as Map<String, Any>)
        val submittedBasicRequirements = SubmittedBasicRequirementsMapper().unmarshal(submittedBasicRequirementsMap)

        @Suppress("UNCHECKED_CAST")

        val submittedRequirementsMap = (properties[KEY_CLAIMS_SUBMITTED_REQUIREMENTS] as Map<String, Any>)
        val submittedRequirements = SubmittedRequirementsMapper().unmarshal(submittedRequirementsMap)

        return Claims(
                identifier = identifier,
                dateCreated = dateCreated,
                source = source,
                createdBy = createdBy,
                type = type,
                dateOfEvent = dateOfEvent,
                claimID = claimID,
                customer = customer,
                insurancePolicies = insurancePolicies,
                benefits = benefits,
                benefitsClaim = benefitsClaims,
                currentStatus = currentStatus,
                pastStatuses = pastStatuses,
                submittedBasicRequirements = submittedBasicRequirements,
                submittedRequirement = submittedRequirements
        )
    }
}