package reach52.marketplace.community.medicine.entity

data class Medicine (
        val id: String,
        val barCode: String,
        val description: String,
        val dosage: String,
        val form: String,
        val handling: String,
        val information: String,
//        val ingredients: List<String>,
        val isoCountry: String,
        val isoCurrency: String,
        val manufacturer: String,
        val medCode: String,
        val brandName: String,
        val genericName: String,
        val packSize: Int,
        val packUnit: String,
        val prescriptionRequired: Boolean,
        val status: String,
        val price: Double,
        val supplierCode: String,
        val tax: Tax,
        val supplier: SupName
){
    data class Tax(
            val category: String,
            val isIncluded: Boolean,
            val percentage: Double,
            val type: String
    )

    data class SupName(
            val eng: String)
}