package reach52.marketplace.community.medicine.mapper

import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.insurance.util.getDouble
import reach52.marketplace.community.insurance.util.getLangText
import reach52.marketplace.community.insurance.util.getString
import reach52.marketplace.community.medicine.entity.Discount
import reach52.marketplace.community.medicine.entity.MedicineOrder
import reach52.marketplace.community.medicine.entity.MedicinePurchase
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class MedicineOrderMapper : Unmarshaler<MedicineOrder>, Marshaler<MedicinePurchase> {
	companion object {
		private val itemMapper = ItemMapper()
	}

	override fun marshal(t: MedicinePurchase): Map<String, Any> {
		val map = HashMap<String, Any>()
		val resident = t.resident
		val physician = t.physician

		map["trackingCode"] = t.trackingCode
		map["isoCountry"] = t.isoCountry
		map["isoCurrency"] = t.isoCurrency
		map["patientAddress"] = mapOf(
				"addressLine1" to resident.addressLine,
				"city" to resident.city,
				"country" to resident.country,
				"isoCountry" to resident.isoCountry,
				"zip" to resident.zipCode
		)
		map["patientName"] = resident.fullName()
		map["patientAge"] = resident.getAge()
		map["patientGender"] = resident.gender
		map["residentId"] = resident.id
		if (t.physician != null) {
			map["physicianId"] = physician!!.id
			map["physicianLicenseNumber"] = physician.licenseNumber
			map["physicianName"] = physician.physicianFullName()
		}
		map["items"] = t.items
		map["suppliers"] = t.suppliers
		map["delivery"] = t.delivery
		map["taxPayable"] = t.taxPayable
		map["orderSubTotal"] = t.subTotal
		map["orderTotalPayable"] = t.orderTotalPayable
		map["currentStatus"] = getStatus("PENDING", t.agent.displayName, t.agent.id)
		map["pastStatuses"] = ArrayList<Map<String, Any>>()
		map["prescriptionNumber"] = t.prescriptionNumber
		map["recipient"] = t.recipient
		map["discountIdNumber"] = t.discountIdNumber

		if (t.discount != null) {
			map["discount"] = mapOf(
					"code" to t.discount.code,
					"name" to t.discount.name,
					"description" to t.discount.desc,
					"amount" to t.discount.value,
					"isPercentage" to t.discount.isPercentage
			)
		} else {
			map["discount"] = HashMap<String, Any>()
		}
		map["type"] = "order"

		map["metadata"] = mapOf(
				"version" to 1,
				"createdBy" to mapOf(
						"userId" to t.agent.id,
						"utcDateTime" to ZonedDateTime.now(ZoneOffset.UTC).toString()
				),
				"updatedBy" to ArrayList<Map<String, Any>>()
		)

		return map
	}

	override fun unmarshal(properties: Map<String, Any>): MedicineOrder {
		val id = properties["_id"] as String
		val residentId = properties["residentId"] as String
		val physicianName = if (properties.containsKey("physicianName")) {
			properties["physicianName"] as String
		} else {
			"NA"
		}
		val status = properties["currentStatus"] as Map<*, *>
		val date = ZonedDateTime.parse(status["statusDate"] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
		val currentStatus = status["status"] as String
		val total = getDouble(properties, "orderTotalPayable")
		val subTotal = getDouble(properties, "orderSubTotal")
		val payableTax = getDouble(properties, "taxPayable")
		val delivery = getDouble(properties, "delivery")
		val items = (properties["items"] as List<*>)
				.filterIsInstance<Map<String, Any>>()
				.map(itemMapper::unmarshal)
		val isoCurrency = properties["isoCurrency"] as String
		val prescriptionNumber = properties["prescriptionNumber"] as String
		val discountIdNumber = properties["discountIdNumber"] as String

		val discountMap = properties["discount"] as Map<String, Any>
		val amt = getDouble(discountMap, "amount")
		val code = getString(discountMap, "code")
		val desc = getString(discountMap, "description")
		val name = getString(discountMap, "name")
		val isPer = discountMap.getOrElse("isPercentage", { true }) as Boolean

		val discount = Discount(code, name, desc, amt, isPer)

		return MedicineOrder(
				id,
				residentId,
				physicianName,
				currentStatus,
				date,
				total,
				subTotal,
				payableTax,
				delivery,
				items,
				isoCurrency,
				prescriptionNumber,
				discountIdNumber,
				discount
		)
	}

	fun getStatus(status: String, userName: String, userId: String) = mapOf(
			"status" to status,
			"statusDate" to ZonedDateTime.now(ZoneOffset.UTC).toString(),
			"userDisplayName" to userName,
			"userId" to userId
	)

	@Suppress("UNCHECKED_CAST")
	class ItemMapper(private val selectedLanguage: String = LanguageUtils.getSavedLanguageInISO3()) : Unmarshaler<MedicineOrder.Item> {
		companion object {
			private val orderTaxMapper = OrderTaxMapper()
		}

		override fun unmarshal(properties: Map<String, Any>): MedicineOrder.Item {
			val brandName = getLangText(properties, "brandName", selectedLanguage)
			val genericName = getLangText(properties, "genericName", selectedLanguage)
			val price = (properties["price"] as Number).toDouble()
			val qtyOriginal = (properties["qtyOriginal"] as Number).toInt()
			val qty = (properties["qty"] as Number).toInt()
			val lineSubTotal = (properties["lineSubTotal"] as Number).toDouble()
			val status = getString(properties, "status")
			val statusReason = getString(properties, "statusReason")
			val dosage = properties["dosage"] as String
			val form = getLangText(properties, "form", selectedLanguage)
			val tax = orderTaxMapper.unmarshal(properties["tax"] as Map<String, Any>)
			val description = if (properties["description"] != "") {
				getLangText(properties, "description", selectedLanguage)
			} else {
				"N/A"
			}

			return MedicineOrder.Item(
					brandName,
					genericName,
					price,
					qtyOriginal,
					qty,
					lineSubTotal,
					status,
					statusReason,
					dosage,
					form,
					tax,
					description
			)
		}

	}
}

class OrderTaxMapper : Unmarshaler<MedicineOrder.Tax> {
	override fun unmarshal(properties: Map<String, Any>): MedicineOrder.Tax {
		val category = properties["category"] as String
		val isIncluded = properties.getOrElse("isIncluded", { false }) as Boolean
		val percentage = (properties["percentage"] as Number).toDouble()
		val type = properties["type"] as String

		return MedicineOrder.Tax(
				category,
				isIncluded,
				percentage,
				type
		)
	}
}