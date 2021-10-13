package reach52.marketplace.community.data

data class LoginResponseModel(
    val code: Int,
    val data: Data,
    val msg: String,
    val success: Boolean,
    val token: String
){
    data class Data(
        val userRole: String,
        val _id: String,
        val address: Address,
        val age: String,
        val createdAt: String,
        val email: String,
        val first_name: String,
        val isNotRegistered: Boolean,
        val last_name: String,
        val mobile_number: String,
        val updatedAt: String
    ) {
        data class Address(
            val addressLine1: String,
            val city: String,
            val country: String,
            val isoCountry: String,
            val location: String,
            val postal_code: String,
            val region: String,
            val town: String,
            val village: String
        )
    }
}