package reach52.marketplace.community.medicine.viewmodel

data class PaymentResponseModel(
    val id : String,
    val external_id: String,
    val user_id: String,
    val status: String,
    val merchant_name: String,
    val merchant_profile_picture_url: String,
    val amount: Long,
    val description: String,
    val expiry_date: String,
    val invoice_url :String,
//    val available_banks: List<AvailableBanks>,
    val should_exclude_credit_card: Boolean,
    val should_send_email: Boolean,
    val created: String,
    val updated: String,
    val currency: String,
//    val items: List<Items>,
//    val customer: List<Customer>

) {

    data class AvailableBanks(
        val ewallet_type: String
    )

    data class Items(
        val name: String,
        val quantity: Int,
        val price: Long
    )

    data class Customer(
        val given_names: String,
        val email: String,
        val mobile_number: String
    )
}