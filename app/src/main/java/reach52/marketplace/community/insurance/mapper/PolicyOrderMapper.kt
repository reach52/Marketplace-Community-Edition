package reach52.marketplace.community.insurance.mapper

import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.extensions.utils.dateTimeStringToZonedDateTime
import reach52.marketplace.community.extensions.utils.zonedDateTimeToString
import reach52.marketplace.community.insurance.entity.*
import reach52.marketplace.community.insurance.util.getFloat
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

// TODO explain why we have marshal and unmarhsal
class PolicyOrderMapper : Marshaler<PolicyPurchase>, Unmarshaler<PolicyOrder> {

	override fun marshal(purchase: PolicyPurchase): Map<String, Any> {

		val product = purchase.product
		val resident = purchase.resident

		val map = HashMap<String, Any>()

		map["type"] = "PolicyOrder"
		map["trackingCode"] = purchase.trackingCode
		map["residentId"] = purchase.resident.id
		map["r52Locale"] = if (product.locale.isEmpty()) "" else product.locale.get(0)
		map["supplier"] = mapOf(
				"suppCode" to purchase.insurer.code,
				"suppId" to purchase.insurer.id,
				"suppCatNo" to product.suppCatNo,
				"suppName" to purchase.insurer.name
		)
		map["catalogItem"] = getCatalogItem(product)
		map["certificateNo"] = ""
		map["isoCountry"] = product.isoCountry
		map["isoCurrency"] = product.isoCurrency
		map["localLang"] = LanguageUtils.getSavedLanguageInISO3()
		map["payment"] = getPayment(purchase)
		map["parties"] = getParties(purchase.members, purchase.idUploadURL)
		map["policyAddress"] = mapOf(
				"addressLine1" to resident.addressLine,
				"city" to resident.city,
				"country" to resident.country,
				"isoCountry" to resident.isoCountry,
				"zip" to resident.zipCode
		)
		map["answers"] = purchase.answers
		map["beneficiary"] = getBeneficiaries(purchase.beneficiaries)
		map["agent"] = mapOf(
				"agentId" to purchase.agent.id,
				"name" to purchase.agent.displayName,
				"email" to purchase.agent.username
		)
		map["orderStatus"] = mapOf(
				"status" to purchase.orderStatus.orderStatus,
				"enteredOn" to zonedDateTimeToString(purchase.orderStatus.enteredOn),
				"processedOn" to zonedDateTimeToString(purchase.orderStatus.processedOn),
				"acceptedOn" to zonedDateTimeToString(purchase.orderStatus.acceptedOn),
				"effectiveDate" to zonedDateTimeToString(purchase.orderStatus.effectiveDate),
				"expiryDate" to zonedDateTimeToString(purchase.orderStatus.expiryDate),
				)

		map["docHistory"] = mapOf(
				"created" to ZonedDateTime.now(ZoneOffset.UTC).toString(),
				"userId" to purchase.agent.id,
				"updated" to ArrayList<Map<String, String>>()
		)


		return map
	}

	private fun getCatalogItem(product: InsuranceProduct): Map<String, Any> {

		val map = HashMap<String, Any>()
		map["catId"] = product.id
		map["r52CatNo"] to product.catNo
		map["r52CatCode"] to product.catCode

		val detailsMap = HashMap<String, Any>()
		detailsMap["name"] = product.displayName
		detailsMap["desc"] = product.displaySummary

		val benefitsList = ArrayList<Map<String, String>>()
		for (benefit in product.benefits) {

			benefitsList += mapOf(
					"name" to benefit.displayName,
					"description" to benefit.displayDesc,
					"exclusion" to benefit.exclusions,
					"amount" to benefit.totalInsured.toString()
			)

		}
		detailsMap["benefits"] = benefitsList
		detailsMap["generalExclusions"] = product.generalExclusions
		detailsMap["terms"] = product.term.toString()

		map["details"] = detailsMap

		return map
	}

	private fun getPayment(purchase: PolicyPurchase): Map<String, Any> {

		val map = HashMap<String, Any>()

		val payment = purchase.payment

		if(payment != null) {
			map["amount"] = payment.amount
			map["plan"] = payment.period
			map["mode"] = payment.mode
			map["paymentStatus"] = payment.paymentStatus
			map["tax"] = mapOf(
					"isIncluded" to purchase.product.tax.isIncluded,
					"percentage" to purchase.product.tax.percentage,
					"type" to purchase.product.tax.type
			)
		}

		return map

	}

	private fun getParties(members: ArrayList<Member>, idUploadURL: String?): List<Map<String, Any>> {

		val parties = ArrayList<Map<String, Any>>()

		for (member in members) {

			val type = member.constructParty.party.code
			val id = HashMap<String, String>()
			if (type == "primary") {
				if(idUploadURL != null) {
					id["image"] = idUploadURL
				}
			}

			parties += mapOf(
					"givenName" to member.firstName,
					"familyName" to member.lastName,
					"dob" to zonedDateTimeToString(member.DOB),
					"gender" to member.gender,
					"type" to member.constructParty.party.code,
					"id" to id,
					"partyName" to member.constructParty.party.displayName,
			)

		}

		return parties
	}

	private fun getBeneficiaries(beneficiaries: List<Beneficiary>): List<Map<String, Any>> {

		val list = ArrayList<Map<String, Any>>()

		for (b in beneficiaries) {
			list += mapOf(
					"givenName" to b.firstName,
					"familyName" to b.lastName,
					"dob" to zonedDateTimeToString(b.DOB),
					"gender" to b.gender,
					"relationship" to b.relation,
					"phone" to b.phone,
					"percentage" to 100F,
					"address" to mapOf(
							"addressLine1" to b.addressLine1,
							"addressLine2" to b.addressLine2,
							"city" to b.city,
							"country" to b.country,
							"zip" to b.zipCode
					)
			)
		}

		return list

	}

	override fun unmarshal(map: Map<String, Any>): PolicyOrder {

		val id = map["_id"] as String
		val trackingCode = map["trackingCode"] as String
		val supplierName = (map["supplier"] as Map<String, String>)["suppName"] as String

		val catalogItem = map["catalogItem"] as Map<String, Any>
		val catId = catalogItem["catId"] as String
		val r52CatNo = catalogItem.getOrElse("r52CatNo", { "" }) as String

		val details = catalogItem["details"] as Map<String, String>
		val name = details["name"] as String
		val desc = details.getOrElse("desc", { "" })
		val terms = details["terms"] as String

		val certificateNo = map["certificateNo"] as String
		val isoCountry = map["isoCountry"] as String
		val isoCurrency = map["isoCurrency"] as String

		val payment = getPayment(map)

		val partiesList = map["parties"] as List<Map<String, Any>>
		val parties = ArrayList<PolicyOrder.Party>()
		partiesList.forEach {

			var url: String? = null
			try {
				val id = it["id"] as Map<String, String>
				url = id["image"] as String
			} catch (e: Exception) {

			}

			val firstName = it["givenName"] as String
			val lastName = it["familyName"] as String

			parties += PolicyOrder.Party(
					"$firstName x $lastName",
					dateTimeStringToZonedDateTime(it["dob"] as String),
					it["gender"] as String,
					it["type"] as String,
					url
			)
		}


		val beneficiaryList = map.getOrElse("beneficiary", {
			ArrayList<Map<String, Any>>()
		}) as List<Map<String, Any>>
		val beneficiaries = ArrayList<Beneficiary>()

		beneficiaryList.forEach {

			val address = it["address"] as Map<String, String>

			beneficiaries += Beneficiary(
					it["givenName"] as String,
					it["familyName"] as String,
					dateTimeStringToZonedDateTime(it["dob"] as String),
					it["gender"] as String,
					it["relationship"] as String,
					it["phone"] as String,
					address["addressLine1"] as String,
					address["addressLine2"] as String,
					address["city"] as String,
					address["country"] as String,
					address["zip"] as String,
			)
		}

		val agentMap = map["agent"] as Map<String, String>
		val orderStatus = (map["orderStatus"] as Map<String, String>)["status"] as String

		var createdDate = "-"
		var lastUpdated = "-"
		try {
			val docHistory = map["docHistory"] as Map<String, Any>
			createdDate = docHistory["created"] as String
			createdDate = ZonedDateTime.parse(createdDate).format(DateTimeFormatter.ISO_LOCAL_DATE)
			val updates = docHistory["updated"] as List<Map<String, String>>
			val latest = updates.last()
			lastUpdated = latest["updated"] as String
			lastUpdated = ZonedDateTime.parse(lastUpdated).format(DateTimeFormatter.ISO_LOCAL_DATE)
		} catch (e: Exception) {

		}

		return PolicyOrder(
				id,
				trackingCode,
				supplierName,
				catId,
				r52CatNo,
				name,
				desc,
				terms,
				certificateNo,
				isoCountry,
				isoCurrency,
				payment,
				parties,
				beneficiaries,
				PolicyOrder.Agent(
						agentMap["agentId"] as String,
						agentMap["name"] as String,
						agentMap["email"] as String
				),
				orderStatus,
				createdDate,
				lastUpdated
		)

	}

	private fun getPayment(map: Map<String, Any>): PolicyPurchase.Payment {


		val payment = map["payment"] as Map<String, Any>
		if (payment.isEmpty()) {
			return PolicyPurchase.Payment(
					0F, "-", "-", "-"
			)
		}
		val amount = getFloat(payment, "amount")
		val period = payment["plan"] as String
		val mode = payment["mode"] as String
		val paymentStatus = payment["paymentStatus"] as String

		return PolicyPurchase.Payment(
				amount, period, mode, paymentStatus
		)

	}

}