package reach52.marketplace.community.medicine.entity

import org.threeten.bp.ZonedDateTime

data class MedicineOrder(
        val id: String,
        val residentId: String,
        val physicianName: String,
        var currentStatus: String,
        val date: ZonedDateTime,
        val total: Double,
        val subTotal: Double,
        val payableTax: Double,
        val deliveryFee: Double,
        val items: List<Item>,
        val isoCurrency: String,
        val prescriptionNumber: String,
        val discountIdNumber: String,
        val discount: Discount? = null
) {
    data class Item(
            val brandName: String,
            val genericName: String,
            val price: Double,
            val qtyOriginal: Int,
            val qty: Int,
            val lineSubTotal: Double,
            val status: String,
            val statusReason: String,
            val dosage: String,
            val form: String,
            val tax: Tax,
            val description: String
    )

    data class Tax(
            val category: String,
            val isIncluded: Boolean,
            val percentage: Double,
            val type: String
    )
}