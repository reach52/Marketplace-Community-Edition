package reach52.marketplace.community.persistence.consumerHealth_mapper

import arrow.core.Option
import reach52.marketplace.community.models.consumer_health_order.Products
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class ProductsMapper: Unmarshaler<Products>, Marshaler<Products> {

    companion object {
        const val KEY_PRODUCT_ID = "productID"
        const val KEY_BRAND = "brand"
        const val KEY_MISC_INFO = "miscInfo"
        const val KEY_UNIT_OF_MEASURE = "unitOfMeasure"
        const val KEY_PRICE = "price"
        const val KEY_VAT = "vatInclusive"
        const val KEY_NAME = "name"
    }

    override fun marshal(t: Products): Map<String, Any> {
        return mapOf(
                KEY_PRODUCT_ID to t.productID,
                KEY_BRAND to t.brand,
                KEY_MISC_INFO to t.miscInfo,
                KEY_UNIT_OF_MEASURE to t.unitOfMeasure,
                KEY_PRICE to t.price,
                KEY_VAT to t.vatInclusive,
                KEY_NAME to t.name
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Products {
        val productID = properties[KEY_PRODUCT_ID] as String
        val brand = properties[KEY_BRAND] as String
        val miscInfo = properties[KEY_MISC_INFO] as String
        val unitOfMeasure = properties[KEY_UNIT_OF_MEASURE] as String
        val price = (properties[KEY_PRICE]as Number).toDouble()
        val isVatInclusive = Option.fromNullable(properties[KEY_VAT]).fold({ false }, { it }) as Boolean
        val name = properties[KEY_NAME] as String

        return Products(
                productID = productID,
                brand = brand,
                miscInfo = miscInfo,
                unitOfMeasure = unitOfMeasure,
                price = price,
                vatInclusive = isVatInclusive,
                name = name
        )
    }
}