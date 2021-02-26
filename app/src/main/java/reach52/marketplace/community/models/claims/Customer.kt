package reach52.marketplace.community.models.claims

import arrow.core.Option

data class Customer(
        val customerID: String,
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val gender: String,
        val age: Int,
        val email: Option<String>,
        val address: String,
        val contact: Option<String>,
        val maritalStatus: String
)