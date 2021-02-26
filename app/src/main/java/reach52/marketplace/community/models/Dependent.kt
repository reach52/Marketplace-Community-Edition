package reach52.marketplace.community.models

import org.threeten.bp.ZonedDateTime

data class Dependent(
		val fullName: String,
		val DOB: ZonedDateTime,
		val gender: String,
		val relation: String
)