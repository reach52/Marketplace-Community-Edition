package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.Dependent
import reach52.marketplace.community.models.policy_owner.PolicyOwner
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class PolicyOwnerMapper : Marshaler<PolicyOwner>, Unmarshaler<PolicyOwner> {
	companion object {
		const val KEY_TYPE = "type"
		const val KEY_POLICY_OWNER_ID = "policyOwnerID"
		const val KEY_POLICY_OWNER_NAME = "policyOwnerFullName"
		const val KEY_ADDRESS = "address"
		const val KEY_EMAIL = "email"
		const val KEY_CONTACT = "contact"
		const val KEY_CIVIL_STATUS = "civilStatus"
		const val KEY_GENDER = "gender"
		const val KEY_BIRTH_DATE = "dateOfBirth"
		const val KEY_INSURED = "insured"
		const val KEY_BENEFICIARY = "beneficiaries"
		const val KEY_INSURER = "insurer"
		const val KEY_STATUS = "status"
		const val KEY_APPLICATION = "application"
		const val KEY_EFFECTIVE_DATE = "effectiveDate"
		const val KEY_EXPIRY = "expiry"
		const val KEY_LAST_UPDATED = "lastUpdated"
		const val KEY_UPDATED_BY = "updatedBy"
		const val KEY_CREATED_BY = "createdBy"
		const val KEY_DEPENDENTS = "dependents"
		const val TYPE_POLICY_OWNER = "policyOwner"
		const val KEY_PAST_STATUS = "pastStatus"

		private val insuredMapper = InsuredMapper()
		private val beneficiariesMapper = BeneficiariesMapper()
		private val insurerMapper = InsurerMapper()
		private val updatedByMapper = UpdatedByMapper()
		private val statusMapper = StatusMapper()
		private val dependentByMapper = DependentMapper()
	}

	override fun marshal(t: PolicyOwner): Map<String, Any> {
		return mapOf(
				KEY_TYPE to t.type,
				KEY_POLICY_OWNER_ID to t.policyOwnerID,
				KEY_POLICY_OWNER_NAME to t.policyOwnerFullName,
				KEY_ADDRESS to t.address,
				KEY_EMAIL to t.email,
				KEY_CONTACT to t.contact,
				KEY_CIVIL_STATUS to t.civilStatus,
				KEY_GENDER to t.gender,
				KEY_BIRTH_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateOfBirth),
				KEY_INSURED to insuredMapper.marshal(t.insured),
				KEY_BENEFICIARY to t.beneficiaries.map(beneficiariesMapper::marshal),
				KEY_PAST_STATUS to t.pastStatuses.map(statusMapper::marshal),
				KEY_INSURER to insurerMapper.marshal(t.insurer),
				KEY_STATUS to t.status,
				KEY_APPLICATION to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.application),
				KEY_EFFECTIVE_DATE to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.effectiveDate),
				KEY_EXPIRY to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.expiry),
				KEY_CREATED_BY to t.createdBy,
				KEY_LAST_UPDATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.lastUpdated),
				KEY_UPDATED_BY to updatedByMapper.marshal(t.updatedBy),
				KEY_DEPENDENTS to t.dependents!!.map(dependentByMapper::marshal)
		)
	}

	override fun unmarshal(properties: Map<String, Any>): PolicyOwner {
		return PolicyOwner(
				type = properties[KEY_TYPE] as String,
				policyOwnerID = properties[KEY_POLICY_OWNER_ID] as String,
				policyOwnerFullName = properties[KEY_POLICY_OWNER_NAME] as String,
				address = properties[KEY_ADDRESS] as String,
				email = properties[KEY_EMAIL] as String,
				contact = properties[KEY_CONTACT] as String,
				civilStatus = properties[KEY_CIVIL_STATUS] as String,
				gender = properties[KEY_GENDER] as String,
				dateOfBirth = ZonedDateTime.parse(properties[KEY_BIRTH_DATE] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
				insured = insuredMapper.unmarshal(properties[KEY_INSURED] as Map<String, Any>),
				beneficiaries = (properties[KEY_BENEFICIARY] as List<*>)
						.filterIsInstance<Map<String, Any>>()
						.map(beneficiariesMapper::unmarshal),
				pastStatuses = (properties[KEY_PAST_STATUS] as List<*>)
						.filterIsInstance<Map<String, Any>>()
						.map(statusMapper::unmarshal),
				insurer = insurerMapper.unmarshal(properties[KEY_INSURER] as Map<String, Any>),
				status = properties[KEY_STATUS] as String,
				application = ZonedDateTime.parse(properties[KEY_APPLICATION] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
				effectiveDate = ZonedDateTime.parse(properties[KEY_APPLICATION] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
				expiry = ZonedDateTime.parse(properties[KEY_EXPIRY] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
				lastUpdated = ZonedDateTime.parse(properties[KEY_APPLICATION] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
				createdBy = properties[KEY_CREATED_BY] as String,
				updatedBy = updatedByMapper.unmarshal(properties[KEY_UPDATED_BY] as Map<String, Any>),
				dependents = (properties[KEY_DEPENDENTS] as List<Dependent>)
						.filterIsInstance<Map<String, Any>>()
						.map(dependentByMapper::unmarshal)

		)
	}
}