package reach52.marketplace.community.models.medication

import arrow.core.Option
import reach52.marketplace.community.models.Discount

data class Order(
//        val currency: String,
//        val currentStatus: OrderStatus,
//        val delivery: Double,
//        val discount: Option<Discount>,
//        val discountIdNumber: Option<String>,
//        val items: OrderItems,
//        val language: String,
//        val lineSubTotal: Double,
//        val orderSubTotal: Double,
//        val orderTotalPayable: Double,
//        val pastStatuses: List<OrderStatus>,
//        val patientAddress: String,
//        val patientAge: Int,
//        val patientGender: String,
//        val patientName: String,
//        val physicianId: String,
//        val physicianLicenseNumber: String,
//        val physicianName: String,
//        val prescriptionNumber: Option<String>,
//        val recipient: Option<String>,
//        val residentId: String,
//        val reqRx: Boolean,
//        val supplier: String,
//        val vatPayable: Double,
//        val createdBy : CreatedBy,
//        val updatedBy: List<CreatedBy>

        val currentStatus: OrderStatus,
        val discount: Option<Discount>,
        val discountIdNumber: Option<String>,
        val items: OrderItems?,
        val pastStatuses: List<OrderStatus>,
        val patientAddress: String,
        val patientAge: Int,
        val patientGender: String,
        val patientName: String,
        val physicianId: String,
        val physicianLicenseNumber: String,
        val physicianName: String,
        val prescriptionNumber: Option<String>,
        val recipient: Option<String>,
        val residentId: String,
        val supplier: String
) {
    // NOTE: Values get resolved in the order they are declared
//    val subTotal: Double = items.list.map { it.basePriceTotal }.sum()
//    val discountAmount: Double = discount.fold({ 0.00 }, { it.discountAmount(subTotal) })
//    val total: Double = discount.fold({ subTotal }, { it.total(subTotal) })
}
