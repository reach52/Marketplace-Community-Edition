package reach52.marketplace.community.models.medication

data class OrderItem(
        val medicationDosage: String,
        val medicationForm: String,
        val medicationId: String,
        val medicationIngredients: List<String>,
        val medicationName: String,
        val medicationPackageSize: Int,
        val price: Double,
        val quantity: Int,
        val isVatExclusive: Boolean
//        val barCode: String,
//        val country: String,
//        val isVatExclusive: Boolean,
//        val localMedicationDosage: String,
//        val localMedicationForm: String,
//        val localMedicationIngredients: String,
//        val localMedicationName: String,
//        val localMedicationSupplier: String,
//        val medCode: String,
//        val medicationDosage: String,
//        val medicationForm: String,
//        val medicationId: String,
//        val medicationIngredients: List<Language>,
//        val medicationName: String,
//        val medicationPackageSize: Int,
//        val price: Double,
//        val r52CatNo: String,
//        val r52SuppCode: String,
//        val suppCatNo: String,
//        val supplier: String,
//        val quantity: Int,
//        val type: String,
//        val vatRate: Double
) {
    val basePriceTotal: Double = price * quantity
    private val vatExempt: Double = if (!isVatExclusive) (price / 1.12) else 0.0
    val vatValue: Double = if (!isVatExclusive) (price - vatExempt) * quantity else 0.0

    fun total(): Double {
        return when (!isVatExclusive) {
            true -> basePriceTotal - (vatValue + discountValue())
            false -> basePriceTotal - discountValue()
        }
    }

    fun discountValue(): Double {
        return when (!isVatExclusive) {
            true -> (vatExempt * .2) * quantity
            false -> (price * .2) * quantity
        }
    }

}