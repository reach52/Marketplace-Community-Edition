package reach52.marketplace.community.insurance.mapper

import reach52.marketplace.community.insurance.entity.InsuranceProduct
import reach52.marketplace.community.insurance.entity.Premium
import reach52.marketplace.community.insurance.util.getFloat
import reach52.marketplace.community.insurance.util.getLangText
import reach52.marketplace.community.insurance.util.getLangTextFromMap
import reach52.marketplace.community.persistence.Unmarshaler

class InsuranceProductMapper(private val selectedLang: String) : Unmarshaler<InsuranceProduct> {
	override fun unmarshal(properties: Map<String, Any>): InsuranceProduct {

		val id = properties["_id"] as String
		val catCode = properties["r52CatCode"] as String
		val catNo = properties["r52CatNo"] as String
		val suppCode = properties["suppCode"] as String
		val suppCatNo = properties["suppCatNo"] as String
		val displayName = getLangText(properties, "name", selectedLang)
		val summary = getLangText(properties, "summary", selectedLang)
		val maxBens = properties.getOrElse("maxBeneficiary", { 1 }) as Int
		val isoCountry = properties["isoCountry"] as String
		val isoCurrency = properties["isoCurrency"] as String
		val locale = properties["r52Locale"] as List<String>
		val term = properties["term"] as Int

		val parties = getPartiesMap(properties)
		val constructs = getConstructs(properties, parties)
		val benefits = getBenefits(properties)
		val generalExclusions = if (properties.containsKey("generalExclusions")) (properties["generalExclusions"] as List<Map<String, String>>).map {
			getLangTextFromMap(it, selectedLang)
		} else ArrayList<String>()
		val tax = getTax(properties)

		val minAge = parties["primary"]!!.minAge
		val maxAge = parties["primary"]!!.maxAge
//		parties.remove("primary")

		val questionsURL = properties.getOrElse("questions", { null }) as String

		return InsuranceProduct(
				id,
				catCode,
				catNo,
				suppCode,
				suppCatNo,
				displayName,
				summary,
				minAge,
				maxAge,
				parties,
				constructs,
				maxBens,
				benefits,
				generalExclusions,
				tax,
				term,
				locale,
				isoCountry,
				isoCurrency,
				questionsURL
		)
	}

	private fun getTax(properties: Map<String, Any>): InsuranceProduct.Tax {
		val taxMap = properties["tax"] as Map<String, Any>
		val taxIncluded = taxMap["isIncluded"] as Boolean
		val taxPercentage = getFloat(taxMap, "percentage")
		val taxType = taxMap["type"] as String
		return InsuranceProduct.Tax(taxIncluded, taxPercentage, taxType)
	}

	private fun getPartiesMap(properties: Map<String, Any>): HashMap<String, InsuranceProduct.Party> {

		val partiesMap = properties["party"] as Map<String, Any>
		val partiesHashMap = HashMap<String, InsuranceProduct.Party>()
		partiesMap.forEach {
			val code = it.key
			val value = it.value as Map<String, Any>
			val name = getLangText(value, "displayName", selectedLang)
			val premium = getPremium(value["premium"] as Map<String, Any>)
			val minAge = getFloat(value, "minAge")
			val maxAge = getFloat(value, "maxAge")
			val party = InsuranceProduct.Party(
					code,
					name,
					premium,
					minAge,
					maxAge
			)

			partiesHashMap[code] = party

		}

		return partiesHashMap

	}

	private fun getConstructs(properties: Map<String, Any>, parties: HashMap<String, InsuranceProduct.Party>): ArrayList<InsuranceProduct.Construct> {

		val constructsList = properties["constructs"] as List<Map<String, Any>>
		val currency = properties["isoCurrency"] as String

		val constructs = ArrayList<InsuranceProduct.Construct>()
		constructsList.forEach {
			val code = it["code"] as String
			val displayName = getLangText(it, "displayName", selectedLang)
			val premium = getPremium(it["premium"] as Map<String, Any>)
			val extraParties = ArrayList<InsuranceProduct.ConstructParty>()

			val extPartyList = it["parties"] as List<Map<String, Any>>
			extPartyList.forEach { map ->
				val party = parties[map["code"] as String]!!
				extraParties += InsuranceProduct.ConstructParty(
						party,
						map["minQuantity"] as Int,
						map["maxQuantity"] as Int
				)
			}


			constructs += InsuranceProduct.Construct(code, displayName, premium, currency, extraParties)
		}
		return constructs

	}

	private fun getBenefits(properties: Map<String, Any>): ArrayList<InsuranceProduct.Benefit> {

		val benefits = ArrayList<InsuranceProduct.Benefit>()

		val benefitList = properties["benefits"] as List<Map<String, Any>>
		benefitList.forEach {

			val name = getLangText(it, "name", selectedLang)
			val desc = getLangText(it, "desc", selectedLang)
			val exclusions = getLangText(it, "exclusions", selectedLang, "-")
			val total = getLangText(it, "totalInsured", selectedLang)
			val currency = properties["isoCurrency"] as String

			benefits += InsuranceProduct.Benefit(
					name,
					desc,
					exclusions,
					total,
					currency
			)
		}

		return benefits

	}

	private fun getPremium(properties: Map<String, Any>): HashMap<String, Premium> {

		val premium = HashMap<String, Premium>()

		properties.keys.forEach { periodKey ->
			val amount = getFloat(properties, periodKey)
			premium[periodKey] = Premium(amount, periodKey)
//			premium["monthly"] = Premium(amount / 10, "monthly") // this only for testing purposes

		}

		return premium
	}

}