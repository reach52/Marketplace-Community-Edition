package reach52.marketplace.community.medicine.entity

data class Discount(
		val code: String,
		val name: String,
		val desc: String,
		val value: Double,
		val isPercentage: Boolean
)