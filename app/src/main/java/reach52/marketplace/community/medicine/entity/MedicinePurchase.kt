package reach52.marketplace.community.medicine.entity

import reach52.marketplace.community.auth.entity.LocalUser
import reach52.marketplace.community.resident.entity.Resident

class MedicinePurchase(
        val isoCountry: String,
        val isoCurrency: String,
        val resident: Resident,
        val physician: Physician?,
        val prescriptionImage: String,
        val items: List<Item>,
        val suppliers: List<Supplier>,
        val delivery: Double,
        val taxPayable: Double,
        val subTotal: Double,
        val orderTotalPayable: Double,
        val prescriptionNumber: String,
        val recipient: String,
        val discountIdNumber: String,
        val discount: Discount?,
        val agent: LocalUser
){
    lateinit var trackingCode: String

    //add supplier here

    data class Item(
            val medicationId: String,
            val suppCatNo: String,
            val prescriptionRequired: Boolean,
            val supplier: String,
            val r52SuppCode: String,
            val information: String,
            val description: String,
            val brandName: String,
            val genericName: String,
            val dosage: String,
            val form: String,
            val ingredients: List<String>,
            val packageSize: Int,
            val packageUnit: String,
            val price: Double,
            val tax: Medicine.Tax,
            val qtyOriginal: Int,
            val qty: Int,
            val lineSubTotal: Double,
            val status: String,
            val statusReason: String
    )

//    data class Discount(
//            val code: String,
//            val discount: Double,
//            val vat: Double
//    )
}