package reach52.marketplace.community.insurance.mapper

import reach52.marketplace.community.insurance.entity.Insurer
import reach52.marketplace.community.insurance.util.getLangText
import reach52.marketplace.community.persistence.Unmarshaler

class InsurerMapper(private val selectedLanguage: String = "eng") : Unmarshaler<Insurer> {

	override fun unmarshal(properties: Map<String, Any>): Insurer {

		val id = properties["_id"] as String
		val name = getLangText(properties, "name", selectedLanguage)
		val code = properties["code"] as String
		val isoCountry = properties["isoCountry"] as String

		val contact = properties["contact"] as Map<String, String>

		val email: String = contact["email"] as String
		val phone: String = contact["phone"] as String

		return Insurer(
				id,
				name,
				code,
				isoCountry,
				email,
				phone
		)

	}
}