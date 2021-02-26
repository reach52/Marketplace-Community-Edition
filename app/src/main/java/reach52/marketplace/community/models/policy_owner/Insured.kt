package reach52.marketplace.community.models.policy_owner

import org.threeten.bp.ZonedDateTime

data class Insured (
        val reference: String,
        val display: String,
        val address: String,
        val email: String,
        val contact: String,
        val civilStatus: String,
        val gender: String,
        val dateOfBirth: ZonedDateTime,
        val relationship: String
)