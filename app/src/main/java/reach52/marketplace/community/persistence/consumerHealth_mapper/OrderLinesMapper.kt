package reach52.marketplace.community.persistence.consumerHealth_mapper

import arrow.core.Option
import reach52.marketplace.community.models.consumer_health_order.OrderLines
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class OrderLinesMapper: Unmarshaler<OrderLines>, Marshaler<OrderLines> {

    companion object{
        const val KEY_ORDER_LINE_ID = "orderLineID"
        const val KEY_BRAND = "brand"
        const val KEY_MISC_INFO = "miscInfo"
        const val KEY_UNIT_OF_MEASURE = "unitOfMeasure"
        const val KEY_QUANTITY = "quantity"
        const val KEY_PRICE = "price"
        const val KEY_IS_VAT_INCLUSIVE = "vatInclusive"
        const val KEY_NAME = "name"
    }

    override fun marshal(t: OrderLines): Map<String, Any> {
        return  mapOf(
                KEY_ORDER_LINE_ID to t.productID,
                KEY_BRAND to t.brand,
                KEY_MISC_INFO to t.miscInfo,
                KEY_UNIT_OF_MEASURE to t.unitOfMeasure,
                KEY_QUANTITY to t.quantity,
                KEY_PRICE to t.price,
                KEY_IS_VAT_INCLUSIVE to t.isVatExclusive,
                KEY_NAME to t.name
        )
    }


    override fun unmarshal(properties: Map<String, Any>): OrderLines {
        val orderLineID = properties[KEY_ORDER_LINE_ID] as String
        val brand = properties[KEY_BRAND] as String
        val miscInfo = properties[KEY_MISC_INFO] as String
        val unitOfMeasure = properties[KEY_UNIT_OF_MEASURE] as String
        val quantity = (properties[KEY_QUANTITY] as Number).toInt()
        val price = (properties[KEY_PRICE]as Number).toDouble()
        val isVatInclusive = Option.fromNullable(properties[ProductsMapper.KEY_VAT]).fold({ false }, { it }) as Boolean
        val name = properties[KEY_NAME] as String

        return OrderLines(
                productID = orderLineID,
                brand = brand,
                miscInfo = miscInfo,
                unitOfMeasure = unitOfMeasure,
                quantity = quantity,
                price = price,
                isVatExclusive = isVatInclusive,
                name = name
        )
    }
}