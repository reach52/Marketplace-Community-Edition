package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Insurer
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class InsurerMapper: Marshaler<Insurer>, Unmarshaler<Insurer> {
    companion object{
        const val KEY_INSURER_NAME = "insurerName"
        const val KEY_REFERENCE_INSURER = "reference"
        const val KEY_CERTIFICATE_NUMBER = "certificateNumber"
        const val KEY_UNIT = "unit"
        const val KEY_PLAN = "plan"
        const val KEY_ATTACHMENT_INSURER = "attachments"
        const val KEY_QUALIFICATION = "qualification"

        private val planMapper = PlanMapper()
        private val qualificationMapper = QualificationMapper()
    }

    override fun marshal(t: Insurer): Map<String, Any> {
        return mapOf(
                KEY_INSURER_NAME to t.insurerName,
                KEY_REFERENCE_INSURER to t.reference,
                KEY_CERTIFICATE_NUMBER to t.certificateNumber,
                KEY_UNIT to t.unit,
                KEY_PLAN to t.plan.map(planMapper::marshal),
                KEY_ATTACHMENT_INSURER to t.attachments,
                KEY_QUALIFICATION to qualificationMapper.marshal(t.qualification)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Insurer {
        val reference = properties[KEY_REFERENCE_INSURER] as String
        val certificateNumber = properties[KEY_CERTIFICATE_NUMBER] as String
        val plan = (properties[KEY_PLAN] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(planMapper::unmarshal)
        val attachments = (properties[KEY_ATTACHMENT_INSURER] as List<*>).map { it as String }
        val qualification = qualificationMapper.unmarshal(properties[KEY_QUALIFICATION] as Map<String, Any>)
        return Insurer(
                insurerName = properties[KEY_INSURER_NAME] as String,
                reference = reference,
                unit = properties[KEY_UNIT] as String,
                certificateNumber = certificateNumber,
                plan = plan,
                attachments = attachments,
                qualification = qualification
        )
    }
}