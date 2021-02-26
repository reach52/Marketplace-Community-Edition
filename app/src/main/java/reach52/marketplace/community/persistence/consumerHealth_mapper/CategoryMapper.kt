package reach52.marketplace.community.persistence.consumerHealth_mapper

import reach52.marketplace.community.models.consumer_health_order.Category
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class CategoryMapper: Marshaler<Category>, Unmarshaler<Category> {

    companion object{
        const val KEY_CATEGORY_ID = "categoryID"
        const val KEY_CATEGORY = "category"
    }

    override fun marshal(t: Category): Map<String, Any> {
        return mapOf(
                KEY_CATEGORY_ID to t.categoryID,
                KEY_CATEGORY to t.category
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Category {
        val categoryID = properties[KEY_CATEGORY_ID] as String
        val category = properties[KEY_CATEGORY] as String
        return Category(
                categoryID = categoryID,
                category = category
        )
    }
}