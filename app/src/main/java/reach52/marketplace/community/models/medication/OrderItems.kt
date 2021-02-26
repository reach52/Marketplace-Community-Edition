package reach52.marketplace.community.models.medication

import reach52.marketplace.community.medicine.entity.MedicinePurchase

data class OrderItems(private val first: MedicinePurchase.Item, private val rest: List<MedicinePurchase.Item>) {
    companion object {
        fun fromList(items: List<MedicinePurchase.Item>): OrderItems = OrderItems(items.first(), items.drop(1))
    }

    val list: List<MedicinePurchase.Item> = mutableListOf(first).also { it += rest }
}
