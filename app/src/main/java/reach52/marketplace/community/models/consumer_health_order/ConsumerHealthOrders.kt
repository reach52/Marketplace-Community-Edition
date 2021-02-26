package reach52.marketplace.community.models.consumer_health_order

import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import com.couchbase.lite.Database
import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.User
import reach52.marketplace.community.models.claims.Customer
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.models.medication.OrderStatus
import reach52.marketplace.community.persistence.consumerHealth_mapper.ConsumerHealthOrderMapper
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class ConsumerHealthOrders(private val database: Database) {

    private val mapper = ConsumerHealthOrderMapper()
    fun residentLogistic(
            discount: Option<Discount>,
            discountIDNumber: Option<String>,
            items: OrderLineItems,
            hcpRequestNumber: Option<String>,
            resident: LogisticResident,
            user: Option<User>
    ): String {
        return database.createDocument().run {
            val currentStatus = OrderStatus(
                    reason = None,
                    status = OrderStatus.Status.PENDING,
                    statusDate = ZonedDateTime.now(ZoneOffset.UTC),
                    userDisplayName = user.map { it.displayName },
                    userId = user.map { it.id },
                    username = user.map { it.username }
            )

            val order = ConsumerHealthOrder(
                    id = "None",
                    currentStatus = currentStatus,
                    discount = discount,
                    discountIDNumber = discountIDNumber,
                    orderLines = items,
                    pastStatuses = listOf(),
                    hcpRequestNumber = hcpRequestNumber,
                    deliveryAddress = resident.address,
                    customer = Customer(
                            resident.residentId,
                            resident.firstName,
                            resident.middleName,
                            resident.lastName,
                            resident.gender,
                            resident.age,
                            email = None,
                            address = resident.address,
                            contact = None,
                            maritalStatus = resident.maritalStatus
                    ),
                    type = ConsumerHealthOrderMapper.KEY_TYPE,
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    createdBy = CreatedBy(
                            userId = user.map { it.username }.getOrElse { "" },
                            userDisplayName = user.map { it.displayName }.getOrElse { "" }
                    ),
                    shoppingId = ""

                    )

            putProperties(mapper.marshal(order).plus("type" to "consumerOrders"))
            id
        }
    }

    fun residentAccess(
            discount: Option<Discount>,
            discountIDNumber: Option<String>,
            items: OrderLineItems,
            hcpRequestNumber: Option<String>,
            resident: Resident,
            user: Option<User>
    ): String{
        return database.createDocument().run {
            val currentStatus = OrderStatus(
                    reason = None,
                    status = OrderStatus.Status.PENDING,
                    statusDate = ZonedDateTime.now(ZoneOffset.UTC),
                    userDisplayName = user.map { it.displayName },
                    userId = user.map { it.id },
                    username = user.map { it.username }
            )

            val order = ConsumerHealthOrder(
                    id = "None",
                    currentStatus = currentStatus,
                    discount = discount,
                    discountIDNumber = discountIDNumber,
                    orderLines = items,
                    pastStatuses = listOf(),
                    hcpRequestNumber = hcpRequestNumber,
                    deliveryAddress = resident.address.text,
                    customer = Customer(
                            resident.id,
                            resident.firstName.getOrElse { "" },
                            resident.lastName.getOrElse { "" },
                            resident.middleName.getOrElse{""},
                            resident.gender,
                            email = None,
                            address = resident.address.text,
                            contact = None,
                            maritalStatus = resident.maritalStatus.toString(),
                            age = resident.age
                            ),
                    type = ConsumerHealthOrderMapper.KEY_TYPE,
                    dateCreated = ZonedDateTime.now(ZoneOffset.UTC),
                    createdBy = CreatedBy(
                            userId = user.map { it.username }.getOrElse { "" },
                            userDisplayName = user.map { it.displayName }.getOrElse { "" }
                    ),
                    shoppingId = ""

            )
            putProperties(mapper.marshal(order).plus("type" to "consumerOrders"))
            id
        }
    }

    fun get(consumerOrderId: String): ConsumerHealthOrder = mapper.unmarshal(database.getDocument(consumerOrderId).properties)

    fun receive(orderId: String, user: Option<User>, callback: (ConsumerHealthOrder) -> Unit) =
            updateStatus(OrderStatus.Status.RECEIVED, orderId, user, callback)

    fun accept(orderId: String, user: Option<User>, callback: (ConsumerHealthOrder) -> Unit) =
            updateStatus(OrderStatus.Status.ACCEPTED, orderId, user, callback)

    fun performPickup(orderId: String, user: Option<User>, callback: (ConsumerHealthOrder) -> Unit) =
            updateStatus(OrderStatus.Status.PICKED_UP, orderId, user, callback)

    fun acceptDelivery(
            orderId: String,
            recipient: Option<String>,
            user: Option<User>,
            callback: (ConsumerHealthOrder) -> Unit
    ) = updateStatus(OrderStatus.Status.DELIVERED, orderId, recipient, user, callback)

    fun rejectDelivery(orderId: String, user: Option<User>, callback: (ConsumerHealthOrder) -> Unit) =
            updateStatus(OrderStatus.Status.NOT_DELIVERED, orderId, user, callback)

    fun hold(orderId: String, user: Option<User>, callback: (ConsumerHealthOrder) -> Unit) =
            updateStatus(OrderStatus.Status.ON_HOLD, orderId, user, callback)

    fun dispatched(orderId: String, user: Option<User>, callback: (ConsumerHealthOrder) -> Unit) =
            updateStatus(OrderStatus.Status.DISPATCHED, orderId, user, callback)

    private fun updateStatus(
            status: OrderStatus.Status,
            orderId: String,
            user: Option<User>,
            callback: (ConsumerHealthOrder) -> Unit
    ) = updateStatus(status, orderId, None, user, callback)


    private fun updateStatus(
            status: OrderStatus.Status,
            orderId: String,
            recipient: Option<String>,
            user: Option<User>,
            callback: (ConsumerHealthOrder) -> Unit

    ) {
        database.getDocument(orderId).update {
            val order = mapper.unmarshal(it.properties)
            val currentStatus =
                    OrderStatus(
                            reason = None,
                            status = status,
                            statusDate = ZonedDateTime.now(ZoneOffset.UTC),
                            userDisplayName = user.map { u -> u.displayName },
                            userId = user.map { u -> u.id },
                            username = user.map { u -> u.username }
                    )


            val pastStatuses = order.pastStatuses.toMutableList().apply { add(order.currentStatus) }
            val newOrder = order.copy(
                    currentStatus = currentStatus,
                    pastStatuses = pastStatuses
            )
            it.userProperties = mapper.marshal(newOrder).plus("type" to "consumerOrders")
            callback(newOrder)
            true
        }
    }
}