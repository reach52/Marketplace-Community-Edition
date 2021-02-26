package reach52.marketplace.community.models.follow_up

import arrow.core.Option
import arrow.core.getOrElse
import com.couchbase.lite.Database
import reach52.marketplace.community.models.User
import reach52.marketplace.community.persistence.followup_mapper.FollowUpMapper
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class FollowUpResident (private val database: Database){

    private val mapper = FollowUpMapper()

    fun new (
            user: Option<User>,
            residentId : String,
            residentName : String,
            email : String,
            contact : String,
            reason : String,
            address : String,
            productDescription : String,
            dateFollowUp : ZonedDateTime
    ): String {
        return database.createDocument().run {
            val createdBy = CreatedBy(
                    userId = user.map { it.username }.getOrElse { "" },
                    userDisplayName = user.map { it.displayName }.getOrElse { "" }
            )

            val followUp = FollowUp(
                    productId = "",
                    productName = "",
                    residentId = residentId,
                    residentName = residentName,
                    email = email,
                    contact = contact,
                    status = "active",
                    fbId = "",
                    reason = reason,
                    notes = "",
                    address = address,
                    productDescription = productDescription,
                    dateFollowUp = dateFollowUp,
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    createdBy = createdBy,
                    type = "followup"
            )

            putProperties(FollowUpMapper().marshal(followUp))
            id
        }
    }

    fun get(followUpId: String): FollowUp = mapper.unmarshal(database.getDocument(followUpId).properties)

    fun updateFollowUp(
            followUpId: String,
            reason: String,
            productDescription: String,
            dateFollowUp: ZonedDateTime
    ){
        database.getDocument(followUpId).update {
            val residentFollowUp = mapper.unmarshal(it.properties)
            val followUp = residentFollowUp.copy(
                    dateFollowUp = dateFollowUp,
                    reason = reason,
                    productDescription = productDescription,
                    status = "active"
            )
            it.userProperties = mapper.marshal(followUp)
            true
        }
    }

    fun addFollowUpNotes(
            notes: String,
            followUpId: String
    ){
        database.getDocument(followUpId).update {
            val residentFollowUp = mapper.unmarshal(it.properties)
            val followUp = residentFollowUp.copy(
                    notes = notes
            )
            it.userProperties = mapper.marshal(followUp)
            true
        }
    }

    fun markFollowUp(
            status: String,
            followUpId: String
    ){
        database.getDocument(followUpId).update {
            val residentFollowUp = mapper.unmarshal(it.properties)
            val followUp = residentFollowUp.copy(
                    status = status
            )
            it.userProperties =mapper.marshal(followUp)
            true
        }
    }
}