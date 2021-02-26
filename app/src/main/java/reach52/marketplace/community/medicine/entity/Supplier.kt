package reach52.marketplace.community.medicine.entity

data class Supplier (
        val id: String,
        val supplierCode: String,
        val isoCountry: String,
        val supplierName: String,
        val r52SuppCode: String,
        val deliveryFee: Double,
        val email: String,
        val phone: String,
        var status: Int // unchecked 0

) {
    override fun toString(): String {
        return "Supplier(id='$id', supplierCode='$supplierCode', isoCountry='$isoCountry', supplierName='$supplierName', r52SuppCode='$r52SuppCode',deliveryFee=$deliveryFee, email='$email', phone='$phone', status=$status)"
    }
}