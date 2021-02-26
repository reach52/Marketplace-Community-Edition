package reach52.marketplace.community.persistence.followup_mapper

import reach52.marketplace.community.models.follow_up.FollowUp
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class FollowUpMapper : Marshaler<FollowUp>, Unmarshaler<FollowUp> {
    companion object {
        const val KEY_PRODUCT_ID = "productId"
        const val KEY_PRODUCT_NAME = "productName"
        const val KEY_RESIDENT_ID = "residentId"
        const val KEY_RESIDENT_NAME = "residentName"
        const val KEY_EMAIL = "email"
        const val KEY_CONTACT = "contact"
        const val KEY_STATUS = "status"
        const val KEY_FB_ID = "fbId"
        const val KEY_REASON = "reason"
        const val KEY_NOTES = "notes"
        const val KEY_ADDRESS = "address"
        const val KEY_PRODUCT_DESCRIPTION = "productDescription"
        const val KEY_DATE_FOLLOW_UP = "dateFollowUp"
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_CREATED_BY = "createdBy"
        const val KEY_TYPE = "type"
        const val TYPE_FOLLOWUP = "followup"

        val createdByMapper = CreatedByMapper()
    }

    override fun marshal(t: FollowUp): Map<String, Any> {
        return mapOf(
                KEY_PRODUCT_ID to t.productId,
                KEY_PRODUCT_NAME to t.productName,
                KEY_RESIDENT_ID to t.residentId,
                KEY_RESIDENT_NAME to t.residentName,
                KEY_EMAIL to t.email,
                KEY_CONTACT to t.contact,
                KEY_STATUS to t.status,
                KEY_FB_ID to t.fbId,
                KEY_REASON to t.reason,
                KEY_NOTES to t.notes,
                KEY_ADDRESS to t.address,
                KEY_PRODUCT_DESCRIPTION to t.productDescription,
                KEY_DATE_FOLLOW_UP to t.dateFollowUp.toString(),
                KEY_DATE_CREATED to t.dateCreated.toString(),
                KEY_CREATED_BY to createdByMapper.marshal(t.createdBy),
                KEY_TYPE to t.type
        )
    }

    override fun unmarshal(properties: Map<String, Any>): FollowUp {
        return FollowUp(
                productId = properties[KEY_PRODUCT_ID] as String,
                productName = properties[KEY_PRODUCT_NAME] as String,
                residentId = properties[KEY_RESIDENT_ID] as String,
                residentName = properties[KEY_RESIDENT_NAME] as String,
                email = properties[KEY_EMAIL] as String,
                contact = properties[KEY_CONTACT] as String,
                status = properties[KEY_STATUS] as String,
                fbId = properties[KEY_FB_ID] as String,
                reason = properties[KEY_REASON] as String,
                notes = properties[KEY_NOTES] as String,
                address = properties[KEY_ADDRESS] as String,
                productDescription = properties[KEY_PRODUCT_DESCRIPTION] as String,
                dateFollowUp = ZonedDateTime.parse(properties[KEY_DATE_FOLLOW_UP]
                        as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                dateCreated = ZonedDateTime.parse(properties[KEY_DATE_CREATED]
                        as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                createdBy = createdByMapper.unmarshal(properties[KEY_CREATED_BY] as Map<String, Any>),
                type = properties[KEY_TYPE] as String
        )
    }
}