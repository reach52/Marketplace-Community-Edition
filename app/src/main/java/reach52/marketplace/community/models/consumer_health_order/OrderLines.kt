package reach52.marketplace.community.models.consumer_health_order

data class OrderLines (
        val productID: String,
        val brand: String,
        val miscInfo: String,
        val unitOfMeasure: String,
        val quantity: Int,
        val price: Double,
        val isVatExclusive: Boolean,
        val name: String
){
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