package reach52.marketplace.community.models.medication

import android.graphics.BitmapFactory
import arrow.core.None
import arrow.core.Option
import com.couchbase.lite.Database
import reach52.marketplace.community.extensions.toPhotoAttachmentStream
import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.User
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.persistence.medication_mapper.OrderMapper
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class Orders(private val database: Database) {
    companion object {
        private const val CONTENT_TYPE_WEBP = "image/webp"
        private const val KEY_PHOTO = "photo"
    }

    private val mapper = OrderMapper()

    fun residentLogistic(
            discount: Option<Discount>,
            discountIdNumber: Option<String>,
            items: OrderItems,
            medication: Medication,
            photoPath: String,
            physician: Physician,
            prescriptionNumber: Option<String>,
            resident: LogisticResident,
            user: Option<User>

    ): String {
        return database.createDocument().run {
            val dateCreated = ZonedDateTime.now(ZoneOffset.UTC)
            val currentStatus = OrderStatus(
                    reason = None,
                    status = OrderStatus.Status.PENDING,
                    statusDate = ZonedDateTime.now(ZoneOffset.UTC),
                    userDisplayName = user.map { it.displayName },
                    userId = user.map { it.id },
                    username = user.map { it.username }
            )

            val order = Order(
//                    currentStatus = currentStatus,
//                    discount = discount,
//                    discountIdNumber = discountIdNumber,
//                    items = items,
//                    pastStatuses = listOf(),
//                    patientAddress = resident.address,
//                    patientAge = resident.age,
//                    patientGender = resident.gender,
//                    patientName = resident.name,
//                    physicianId = physician.id,
//                    physicianLicenseNumber = physician.licenseNumber,
//                    physicianName = physician.name,
//                    prescriptionNumber = prescriptionNumber,
//                    recipient = None,
//                    residentId = resident.residentId,
//                    supplier = medication.suppliers.toString(),
//                    currency = "",
//                    language = "",
//                    createdBy = CreatedBy(
//                            dateTime = dateCreated,
//                            userId = user.map { it.username }.getOrElse { "" }
//                    ),
//                    updatedBy = listOf(),
//                    delivery = 20.00,
//                    lineSubTotal = 40.00,
//                    orderSubTotal = 80.00,
//                    vatPayable = 20.0,
//                    orderTotalPayable = 100.00,
//                    reqRx = true

                    currentStatus = currentStatus,
                    discount = discount,
                    discountIdNumber = discountIdNumber,
                    items = items,
                    pastStatuses = listOf(),
                    patientAddress = resident.address,
                    patientAge = resident.age,
                    patientGender = resident.gender,
                    patientName = resident.name,
                    physicianId = physician.id,
                    physicianLicenseNumber = physician.licenseNumber,
                    physicianName = physician.name,
                    prescriptionNumber = prescriptionNumber,
                    recipient = None,
                    residentId = resident.residentId,
                    supplier = medication.supplier


            )
            putProperties(mapper.marshal(order).plus("type" to "order"))
            BitmapFactory.decodeFile(photoPath)?.toPhotoAttachmentStream().let {
                createRevision().apply {
                    setAttachment(KEY_PHOTO, CONTENT_TYPE_WEBP, it)
                    save()
                }
                it?.close()
            }
            id
        }
    }

    fun residentAccess(
            discount: Option<Discount>,
            discountIdNumber: Option<String>,
            items: OrderItems,
            medication: Medication,
            photoPath: String,
            physician: Physician,
            prescriptionNumber: Option<String>,
            resident: Resident,
            user: Option<User>
    ): String {
        return database.createDocument().run {
            val dateCreated = ZonedDateTime.now(ZoneOffset.UTC)
            val currentStatus = OrderStatus(
                    reason = None,
                    status = OrderStatus.Status.PENDING,
                    statusDate = ZonedDateTime.now(ZoneOffset.UTC),
                    userDisplayName = user.map { it.displayName },
                    userId = user.map { it.id },
                    username = user.map { it.username }
            )

            val order = Order(
//                    currentStatus = currentStatus,
//                    discount = discount,
//                    discountIdNumber = discountIdNumber,
//                    items = items,
//                    pastStatuses = listOf(),
//                    patientAddress = resident.address.text,
//                    patientAge = resident.age,
//                    patientGender = resident.gender,
//                    patientName = resident.name,
//                    physicianId = physician.id,
//                    physicianLicenseNumber = physician.licenseNumber,
//                    physicianName = physician.name,
//                    prescriptionNumber = prescriptionNumber,
//                    recipient = None,
//                    residentId = resident.id,
//                    supplier = medication.suppliers.toString(),
//                    currency = "",
//                    language = "",
//                    createdBy = CreatedBy(
//                            dateTime = dateCreated,
//                            userId = user.map { it.username }.getOrElse { "" }
//                    ),
//                    updatedBy = listOf(),
//                    delivery = 20.00,
//                    lineSubTotal = 40.00,
//                    orderSubTotal = 80.00,
//                    vatPayable = 20.0,
//                    orderTotalPayable = 100.00,
//                    reqRx = true

                    currentStatus = currentStatus,
                    discount = discount,
                    discountIdNumber = discountIdNumber,
                    items = items,
                    pastStatuses = listOf(),
                    patientAddress = resident.address.text,
                    patientAge = resident.age,
                    patientGender = resident.gender,
                    patientName = resident.name,
                    physicianId = physician.id,
                    physicianLicenseNumber = physician.licenseNumber,
                    physicianName = physician.name,
                    prescriptionNumber = prescriptionNumber,
                    recipient = None,
                    residentId = resident.id,
                    supplier = medication.supplier
            )
            putProperties(mapper.marshal(order).plus("type" to "order"))
            BitmapFactory.decodeFile(photoPath)?.toPhotoAttachmentStream().let {
                createRevision().apply {
                    setAttachment(KEY_PHOTO, CONTENT_TYPE_WEBP, it)
                    save()
                }
                it?.close()
            }
            id
        }
    }

    fun get(orderId: String): Order = mapper.unmarshal(database.getDocument(orderId).properties)
    fun receive(orderId: String, user: Option<User>, callback: (Order) -> Unit) =
            updateStatus(OrderStatus.Status.RECEIVED, orderId, user, callback)

    fun accept(orderId: String, user: Option<User>, callback: (Order) -> Unit) =
            updateStatus(OrderStatus.Status.ACCEPTED, orderId, user, callback)

    fun performPickup(orderId: String, user: Option<User>, callback: (Order) -> Unit) =
            updateStatus(OrderStatus.Status.PICKED_UP, orderId, user, callback)

    fun acceptDelivery(
            orderId: String,
            recipient: Option<String>,
            user: Option<User>,
            callback: (Order) -> Unit
    ) = updateStatus(OrderStatus.Status.DELIVERED, orderId, recipient, user, callback)

    fun rejectDelivery(orderId: String, user: Option<User>, callback: (Order) -> Unit) =
            updateStatus(OrderStatus.Status.NOT_DELIVERED, orderId, user, callback)

    fun hold(orderId: String, user: Option<User>, callback: (Order) -> Unit) =
            updateStatus(OrderStatus.Status.ON_HOLD, orderId, user, callback)

    fun dispatched(orderId: String, user: Option<User>, callback: (Order) -> Unit) =
            updateStatus(OrderStatus.Status.DISPATCHED, orderId, user, callback)

    private fun updateStatus(
            status: OrderStatus.Status,
            orderId: String,
            user: Option<User>,
            callback: (Order) -> Unit
    ) = updateStatus(status, orderId, None, user, callback)

    private fun updateStatus(
            status: OrderStatus.Status,
            orderId: String,
            recipient: Option<String>,
            user: Option<User>,
            callback: (Order) -> Unit

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
                    pastStatuses = pastStatuses,
                    recipient = recipient
            )
            it.userProperties = mapper.marshal(newOrder).plus("type" to "order")
            callback(newOrder)
            true
        }
    }
}