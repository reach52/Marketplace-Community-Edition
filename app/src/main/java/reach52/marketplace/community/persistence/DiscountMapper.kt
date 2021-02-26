package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.Discount

class DiscountMapper : Marshaler<Discount>, Unmarshaler<Discount> {
    companion object {
        const val KEY_CODE = "code"
        const val KEY_DISCOUNT = "discount"
        const val KEY_VAT = "vat"
    }

    override fun marshal(t: Discount): Map<String, Any> = mapOf(
            KEY_CODE to t.code,
            KEY_DISCOUNT to t.discount,
            KEY_VAT to t.vat
    )

    override fun unmarshal(properties: Map<String, Any>): Discount = Discount(
            code = properties[KEY_CODE] as String,
            discount = (properties[KEY_DISCOUNT] as Number).toDouble(),
            vat = (properties[KEY_VAT] as Number).toDouble()
    )
}