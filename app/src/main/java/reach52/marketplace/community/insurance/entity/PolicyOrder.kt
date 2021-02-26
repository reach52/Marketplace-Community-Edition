package reach52.marketplace.community.insurance.entity

import org.threeten.bp.ZonedDateTime

data class PolicyOrder(
		val id: String,
		val trackingCode: String,
		val insurerName: String,
		val catId: String,
		val r52CatNo: String,
		val productName: String,
		val productDesc: String,
		val terms: String,
		val certificateNo: String,
		val isoCountry: String,
		val isoCurrency: String,
		val payment: PolicyPurchase.Payment,
		val parties: List<Party>,
		val beneficiary: List<Beneficiary>,
		val agent: Agent,
		val orderStatus: String,
		val createdOn: String,
		val updatedOn: String = "-",
) {

	data class Party(
			val name: String,
			val DOB: ZonedDateTime,
			val gender: String,
			val type: String,
			val uploadedIDURL: String?
	)

	data class Agent(
			val id: String,
			val name: String,
			val email: String
	)

}