package reach52.marketplace.community.medicine.viewmodel

data class Data(
    val amount: String,
    val available_banks: List<Any>,
    val available_ewallets: List<AvailableEwallet>,
    val created: String,
    val currency: String,
    val description: String,
    val expiry_date: String,
    val external_id: String,
    val id: String,
    val invoice_url: String,
    val merchant_name: String,
    val merchant_profile_picture_url: String,
    val payer_email: String,
    val should_exclude_credit_card: Boolean,
    val should_send_email: Boolean,
    val status: String,
    val updated: String,
    val user_id: String
)