package reach52.marketplace.community.insurance.entity

import reach52.marketplace.community.auth.entity.LocalUser
import reach52.marketplace.community.resident.entity.Resident
import org.threeten.bp.ZonedDateTime

class PolicyPurchase(
		val insurer: Insurer,
		val product: InsuranceProduct,
		val members: ArrayList<Member>,
		val beneficiaries: ArrayList<Beneficiary>,
		val payment: Payment?,
		val idUploadURL: String?,
		val resident: Resident,
		val answers: HashMap<String, String>,
		val agent: LocalUser
) {
	lateinit var trackingCode: String
	lateinit var certificateNo: String
	lateinit var orderStatus: OrderStatus
	lateinit var docHistory: DocHistory

	data class Payment(
			val amount: Float,
			val period: String,
			val mode: String = "cash",
			val paymentStatus: String = "paid"
	)

	class OrderStatus {
		var orderStatus: String = REQUESTED
		lateinit var enteredOn: ZonedDateTime
		var processedOn: ZonedDateTime? = null
		var acceptedOn: ZonedDateTime? = null
		var effectiveDate: ZonedDateTime? = null
		var expiryDate: ZonedDateTime? = null

		companion object{
			const val REQUESTED = "requested"
		}

	}

	data class DocHistory(
			val created: ZonedDateTime,
			val userId: String,
			val updated: List<Update>
	)

	data class Update(
			val status: String,
			val updated: ZonedDateTime,
			val userId: String
	)

}