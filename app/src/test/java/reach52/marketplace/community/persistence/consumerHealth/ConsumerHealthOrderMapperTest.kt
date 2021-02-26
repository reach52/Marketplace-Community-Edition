package reach52.marketplace.community.persistence.consumerHealth

import arrow.core.None
import reach52.marketplace.community.models.medication.OrderStatus
import reach52.marketplace.community.models.claims.Customer
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrder
import reach52.marketplace.community.models.consumer_health_order.CreatedBy
import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.models.consumer_health_order.OrderLines
import reach52.marketplace.community.persistence.OrderStatusMapper
import reach52.marketplace.community.persistence.claims_mapper.CustomerMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.ConsumerHealthOrderMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.OrderLinesMapper
import reach52.marketplace.community.persistence.consumerHealth_mapper.CreatedByMapper
import org.junit.Assert
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ConsumerHealthOrderMapperTest {

    private val mapper = ConsumerHealthOrderMapper()
    private val orderFixture = ConsumerHealthOrder(
            id = "id123",
            dateCreated = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            createdBy = CreatedBy(
                    userId = "juan@email.com",
                    userDisplayName = "Juan Dela Cruz"
            ),
            type = "order",
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
            currentStatus = OrderStatus(
                    status = OrderStatus.Status.PENDING,
                    statusDate = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    username = None,
                    userDisplayName = None,
                    userId = None,
                    reason = None
            ),
            pastStatuses = listOf(
                    OrderStatus(
                            status = OrderStatus.Status.PENDING,
                            statusDate = ZonedDateTime.parse("2020-07-05T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                            username = None,
                            userDisplayName = None,
                            userId = None,
                            reason = None
                    )
            ),
            discountIDNumber = None,
            discount = None,
            hcpRequestNumber = None,
            orderLines = OrderLineItems(
                OrderLines(
                        productID = "id123",
                        brand = "brand",
                        miscInfo = "info",
                        unitOfMeasure = "free",
                        quantity = 1,
                        price = 200.00,
                        isVatExclusive = false,
                        name = "Product name"
                ), listOf()
            ),
            deliveryAddress = "Iloilo",
            shoppingId = "id123"
    )

    private val mapFixture = mapOf(
            ConsumerHealthOrderMapper.KEY_ID to "id123",
            ConsumerHealthOrderMapper.KEY_DATE_CREATED to "2020-07-05T00:00:00Z",
            ConsumerHealthOrderMapper.KEY_TYPE to "order",
            ConsumerHealthOrderMapper.KEY_CREATED_BY to  mapOf(
                    CreatedByMapper.KEY_USER_ID to "juan@email.com",
                    CreatedByMapper.KEY_USER_DISPLAY_NAME to "Juan Dela Cruz"
            ),
            ConsumerHealthOrderMapper.KEY_CUSTOMER to mapOf(
                    CustomerMapper.KEY_CUSTOMER_ID to "customerID",
                    CustomerMapper.KEY_CUSTOMER_FIRST_NAME to "firstName",
                    CustomerMapper.KEY_CUSTOMER_MIDDLE_NAME to "middleName",
                    CustomerMapper.KEY_CUSTOMER_LAST_NAME to "lastName",
                    CustomerMapper.KEY_CUSTOMER_GENDER to "gender",
                    CustomerMapper.KEY_CUSTOMER_AGE to 20,
                    CustomerMapper.KEY_CUSTOMER_ADDRESS to "address",
                    CustomerMapper.KEY_CUSTOMER_MARITAL_STATUS to "maritalStatus"
            ),
            ConsumerHealthOrderMapper.KEY_CURRENT_STATUS to mapOf(
                    OrderStatusMapper.KEY_STATUS to "PENDING",
                    OrderStatusMapper.KEY_STATUS_DATE to "2020-07-05T00:00:00Z"
            ),
            ConsumerHealthOrderMapper.KEY_PAST_STATUSES to listOf(
                    mapOf(
                            OrderStatusMapper.KEY_STATUS to "PENDING",
                            OrderStatusMapper.KEY_STATUS_DATE to "2020-07-05T00:00:00Z"
                    )
            ),
            ConsumerHealthOrderMapper.KEY_ORDER_LINES to
                    listOf(
                        mapOf(
                            OrderLinesMapper.KEY_ORDER_LINE_ID to "id123",
                            OrderLinesMapper.KEY_BRAND to "brand",
                            OrderLinesMapper.KEY_MISC_INFO to "info",
                            OrderLinesMapper.KEY_UNIT_OF_MEASURE to "free",
                            OrderLinesMapper.KEY_QUANTITY to 1,
                            OrderLinesMapper.KEY_PRICE to 200.00,
                            OrderLinesMapper.KEY_IS_VAT_INCLUSIVE to false,
                                OrderLinesMapper.KEY_NAME to "Product name"
                        )
                    ),
            ConsumerHealthOrderMapper.KEY_DELIVERY_ADDRESS to "Iloilo",
            ConsumerHealthOrderMapper.KEY_SHOPPING_ID to "id123"
    )

    @Test
    fun marshal() {
        Assert.assertEquals(mapFixture, mapper.marshal(orderFixture))
    }

    @Test
    fun unmarshal() {
        Assert.assertEquals(orderFixture, mapper.unmarshal(mapFixture))
    }
}