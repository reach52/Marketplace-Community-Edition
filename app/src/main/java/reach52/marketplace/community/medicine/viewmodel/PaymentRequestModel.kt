package reach52.marketplace.community.medicine.viewmodel

data class PaymentRequestModel(
    val externalID: String?,
    val amount: Double,
    val description: String,
    var should_send_email: Boolean,
    val payer_email: String?,
    val customer: Customers,
    val currency: String,
    val items: List<Items>
){
    data class Customers(
        val customer: String,
        val email: String? = null,
        val mobile_number: String
    )

    data class Items(
        val name: String,
        val quantity: Int,
        val price: Long
    )
}