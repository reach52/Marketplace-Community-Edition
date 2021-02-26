package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.SubmittedRequirements
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class SubmittedRequirementsMapper : Marshaler<SubmittedRequirements>, Unmarshaler<SubmittedRequirements> {
    companion object {
        const val KEY_REQUIREMENTS_CODE = "code"
        const val KEY_REQUIREMENTS_TYPE = "type"
        const val KEY_REQUIREMENTS_DATE_SUBMITTED = "dateSubmitted"
        const val KEY_REQUIREMENTS_ATTACHMENT_NAME = "attachmentName"
    }

    override fun marshal(t: SubmittedRequirements): Map<String, Any> {
        return mapOf(
                KEY_REQUIREMENTS_CODE to t.code,
                KEY_REQUIREMENTS_TYPE to t.type,
                KEY_REQUIREMENTS_DATE_SUBMITTED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateSubmitted),
                KEY_REQUIREMENTS_ATTACHMENT_NAME to t.attachmentName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): SubmittedRequirements {
        val code = properties[KEY_REQUIREMENTS_CODE] as String
        val type = properties[KEY_REQUIREMENTS_TYPE] as String
        val dateSubmitted = ZonedDateTime.parse(properties[KEY_REQUIREMENTS_DATE_SUBMITTED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val attachmentName = properties[KEY_REQUIREMENTS_ATTACHMENT_NAME] as String

        return SubmittedRequirements(
                code = code,
                type = type,
                dateSubmitted = dateSubmitted,
                attachmentName = attachmentName
        )
    }

}