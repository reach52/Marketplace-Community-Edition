package reach52.marketplace.community.insurance.entity

import org.threeten.bp.ZonedDateTime

class Member (
	val firstName: String,
	val lastName: String,
	val DOB: ZonedDateTime,
	val gender: String,
	val constructParty: InsuranceProduct.ConstructParty
)