package reach52.marketplace.community.medicine.mapper

import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.insurance.util.getDouble
import reach52.marketplace.community.insurance.util.getLangText
import reach52.marketplace.community.medicine.entity.Discount
import reach52.marketplace.community.persistence.Unmarshaler

class DiscountMapper(val selectedLang: String = LanguageUtils.getSavedLanguageInISO3()) : Unmarshaler<Discount> {

	override fun unmarshal(properties: Map<String, Any>): Discount {

		val name = properties["name"] as String
		val desc = getLangText(properties, "description", selectedLang)
		val value = getDouble(properties, "amount")
		val isPercentage = properties["isPercentage"] as Boolean

		return Discount(
				name,
				name,
				desc,
				value,
				isPercentage
		)

	}
}