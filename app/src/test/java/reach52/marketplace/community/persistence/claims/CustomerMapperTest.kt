package reach52.marketplace.community.persistence.claims

import arrow.core.None
import reach52.marketplace.community.models.claims.Customer
import reach52.marketplace.community.persistence.claims_mapper.CustomerMapper
import org.junit.Assert
import org.junit.Test

class CustomerMapperTest {

    private val claimsCustomerModelFixture = Customer(
            customerID = "customerID",
            firstName = "firstName",
            middleName = "middleName",
            lastName = "lastName",
            gender = "gender",
            age = 20,
            email = None,
            address = "address",
            contact = None,
            maritalStatus = "maritalStatus"
    )

    private val claimsCustomerMapFixture = mapOf(
            CustomerMapper.KEY_CUSTOMER_ID to "customerID",
            CustomerMapper.KEY_CUSTOMER_FIRST_NAME to "firstName",
            CustomerMapper.KEY_CUSTOMER_MIDDLE_NAME to "middleName",
            CustomerMapper.KEY_CUSTOMER_LAST_NAME to "lastName",
            CustomerMapper.KEY_CUSTOMER_GENDER to "gender",
            CustomerMapper.KEY_CUSTOMER_AGE to 20,
            CustomerMapper.KEY_CUSTOMER_ADDRESS to "address",
            CustomerMapper.KEY_CUSTOMER_MARITAL_STATUS to "maritalStatus"
    )

    @Test
    fun unmarshal() {
        Assert.assertEquals(claimsCustomerModelFixture, CustomerMapper().unmarshal(claimsCustomerMapFixture))
    }


    @Test
    fun marshal() {
        Assert.assertEquals(claimsCustomerMapFixture, CustomerMapper().marshal(claimsCustomerModelFixture))
    }
}