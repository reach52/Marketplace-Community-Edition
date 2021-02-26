package reach52.marketplace.community.persistence.claims_mapper

import arrow.core.Option
import reach52.marketplace.community.models.claims.Customer
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class CustomerMapper : Marshaler<Customer>, Unmarshaler<Customer> {
    companion object {
        const val KEY_CUSTOMER_ID = "customerID"
        const val KEY_CUSTOMER_FIRST_NAME = "firstName"
        const val KEY_CUSTOMER_MIDDLE_NAME = "middleName"
        const val KEY_CUSTOMER_LAST_NAME = "lastName"
        const val KEY_CUSTOMER_GENDER = "gender"
        const val KEY_CUSTOMER_AGE = "age"
        const val KEY_CUSTOMER_EMAIL_ADDRESS = "email"
        const val KEY_CUSTOMER_ADDRESS = "address"
        const val KEY_CUSTOMER_CONTACT = "09876554321"
        const val KEY_CUSTOMER_MARITAL_STATUS = "maritalStatus"
    }

    override fun marshal(t: Customer): Map<String, Any> = mutableMapOf(KEY_CUSTOMER_ID to t.customerID, KEY_CUSTOMER_FIRST_NAME to t.firstName,
            KEY_CUSTOMER_MIDDLE_NAME to t.middleName, KEY_CUSTOMER_LAST_NAME to t.lastName, KEY_CUSTOMER_GENDER to t.gender,
            KEY_CUSTOMER_AGE to t.age, KEY_CUSTOMER_ADDRESS to t.address, KEY_CUSTOMER_MARITAL_STATUS to t.maritalStatus).also {

        t.email.map { emailAddress ->
            it[KEY_CUSTOMER_EMAIL_ADDRESS] = emailAddress
        }

        t.contact.map { contact ->
            it[KEY_CUSTOMER_CONTACT] = contact
        }
    }

    override fun unmarshal(properties: Map<String, Any>): Customer {
        val customerID = properties[KEY_CUSTOMER_ID] as String
        val firstName = properties[KEY_CUSTOMER_FIRST_NAME] as String
        val middleName = properties[KEY_CUSTOMER_MIDDLE_NAME] as String
        val lastName = properties[KEY_CUSTOMER_LAST_NAME] as String
        val gender = properties[KEY_CUSTOMER_GENDER] as String
        val age = properties[KEY_CUSTOMER_AGE] as Int
        val email = Option.fromNullable(properties[KEY_CUSTOMER_EMAIL_ADDRESS]).map { it as String }
        val address = properties[KEY_CUSTOMER_ADDRESS] as String
        val contact = Option.fromNullable(properties[KEY_CUSTOMER_CONTACT]).map { it as String }
        val maritalStatus = properties[KEY_CUSTOMER_MARITAL_STATUS] as String

        return Customer(
                customerID = customerID,
                firstName = firstName,
                middleName = middleName,
                lastName = lastName,
                gender = gender,
                age = age,
                email = email,
                address = address,
                contact = contact,
                maritalStatus = maritalStatus)
    }
}
