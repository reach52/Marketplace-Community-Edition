package reach52.marketplace.community.insurance.entity

import org.threeten.bp.ZonedDateTime

data class Beneficiary(
		val firstName: String,
		val lastName: String,
		val DOB: ZonedDateTime,
		val gender: String,
		val relation: String,
		val phone: String,
		val addressLine1: String,
		val addressLine2: String,
		val city: String,
		val country: String,
		val zipCode: String,
)