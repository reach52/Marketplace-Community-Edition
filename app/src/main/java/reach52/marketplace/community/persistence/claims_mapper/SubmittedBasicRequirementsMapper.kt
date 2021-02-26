package reach52.marketplace.community.persistence.claims_mapper

import reach52.marketplace.community.models.claims.SubmittedBasicRequirements
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class SubmittedBasicRequirementsMapper : Marshaler<SubmittedBasicRequirements>, Unmarshaler<SubmittedBasicRequirements> {
    companion object {
        const val KEY_BASIC_REQ_CODE = "code"
        const val KEY_BASIC_REQ_TYPE = "type"
        const val KEY_BASIC_REQ_DATE_SUBMITTED = "dateSubmitted"
        const val KEY_BASIC_REQ_ATTACHMENT_NAME = "attachmentName"
    }

    override fun marshal(t: SubmittedBasicRequirements): Map<String, Any> {
        return mapOf(
                KEY_BASIC_REQ_CODE to t.code,
                KEY_BASIC_REQ_TYPE to t.type,
                KEY_BASIC_REQ_DATE_SUBMITTED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateSubmitted),
                KEY_BASIC_REQ_ATTACHMENT_NAME to t.attachmentName
        )
    }

    override fun unmarshal(properties: Map<String, Any>): SubmittedBasicRequirements {
        val code = properties[KEY_BASIC_REQ_CODE] as String
        val type = properties[KEY_BASIC_REQ_TYPE] as String
        val dateSubmitted = ZonedDateTime.parse(properties[KEY_BASIC_REQ_DATE_SUBMITTED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val attachmentName = properties[KEY_BASIC_REQ_ATTACHMENT_NAME] as String

        return SubmittedBasicRequirements(
                code = code,
                type = type,
                dateSubmitted = dateSubmitted,
                attachmentName = attachmentName
        )
    }
}