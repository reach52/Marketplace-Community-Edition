package reach52.marketplace.community.models.consumer_health_order

data class Products(
        val productID: String,
        val brand: String,
        val miscInfo: String,
        val unitOfMeasure: String,
        val price: Double,
        val vatInclusive: Boolean,
        val name: String
)