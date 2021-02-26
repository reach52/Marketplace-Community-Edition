package reach52.marketplace.community.models

import arrow.core.None
import arrow.core.Some
import reach52.marketplace.community.models.claims.Customer
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrder
import reach52.marketplace.community.models.consumer_health_order.CreatedBy
import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.models.consumer_health_order.OrderLines
import reach52.marketplace.community.models.medication.OrderStatus
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ConsumerHealthOrderTest {

    private val orderFixture = ConsumerHealthOrder(
            id = "id123",
            dateCreated = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            createdBy = CreatedBy(
                    userId = "juan@email.com",
                    userDisplayName = "Juan Dela Cruz"
            ),
            currentStatus = OrderStatus(
                    status = OrderStatus.Status.PENDING,
                    statusDate = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    username = None,
                    userDisplayName = None,
                    userId = None,
                    reason = None
            ),
            discount = Some(Discount("TEST", 0.20, 0.12)),
            discountIDNumber = Some("TEST"),
            hcpRequestNumber = None,
            customer = Customer(
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
            ),
            pastStatuses = listOf(),
            orderLines = OrderLineItems(
                    OrderLines(
                            productID = "id123",
                            brand = "brand",
                            miscInfo = "info",
                            unitOfMeasure = "free",
                            quantity = 2,
                            price = 250.00,
                            isVatExclusive = false,
                            name = "Product name"
                    ), listOf()
            ),
            deliveryAddress = "Iloilo",
            type = "order",
            shoppingId = "id123"
    )

    @Test
    fun total() {
        Assert.assertEquals(500.00.toBits(), orderFixture.subTotal.toBits())
    }

    @Test
    fun discountAmount() {
        Assert.assertEquals(89.29.toBits(), orderFixture.discountAmount.toBits())
    }

    @Test
    fun billableAmount() {
        Assert.assertEquals(357.14.toBits(), orderFixture.total.toBits())
    }
}