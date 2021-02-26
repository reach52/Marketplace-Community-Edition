package reach52.marketplace.community.resident.mapper

import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import reach52.marketplace.community.resident.entity.Resident
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class ResidentMapper : Marshaler<Resident>, Unmarshaler<Resident> {

	private val KEY_RESIDENT_ID = "residentId"
	private val KEY_FIRST_NAME = "givenName"
	private val KEY_MIDDLE_NAME = "middleName"
	private val KEY_LAST_NAME = "familyName"
	private val KEY_EMAIL = "email"
	private val KEY_GENDER = "gender"
	private val KEY_MARITAL_STATUS = "martialStatus"
	private val KEY_PHONE = "phone"
	private val KEY_DATE_OF_BIRTH = "dateOfBirth"
	private val KEY_ADDRESS_LINE = "addressLine1"
	private val KEY_ZIP_CODE = "zip"
	private val KEY_CITY = "city"
	private val KEY_COUNTRY = "country"
	private val KEY_ISO_COUNTRY = "isoCountry"

	private val KEY_CREATED_BY = "createdBy"
	private val KEY_UPDATED_BY = "updatedBy"

	override fun marshal(resident: Resident): Map<String, Any> {

		val map = HashMap<String, Any>()

		val basic = mapOf(
				KEY_FIRST_NAME to resident.firstName,
				KEY_LAST_NAME to resident.lastName,
				KEY_MIDDLE_NAME to resident.middleName,
				KEY_GENDER to resident.gender,
				KEY_MARITAL_STATUS to resident.maritalStatus,
				KEY_DATE_OF_BIRTH to resident.dob.toString(),
				KEY_PHONE to resident.phone,
				KEY_EMAIL to resident.email,
				"address" to mapOf(
						KEY_ADDRESS_LINE to resident.addressLine,
						KEY_CITY to resident.city,
						KEY_COUNTRY to resident.country,
						KEY_ISO_COUNTRY to resident.isoCountry,
						KEY_ZIP_CODE to resident.zipCode
				)
		)

		val questions = ArrayList<Map<String, Any>>()

		for ((k, v) in resident.questions) {

			val ansMap = mapOf(
					"questionCode" to k,
					"answer" to v
			)
			questions.add(ansMap)

		}

//		val reviewResponse = ArrayList<Map<String, Any>>()
//
//		for ((k, v) in resident.reviewResponse) {
//
//			val ansMap = mapOf(
//					"questionCode" to k,
//					"answer" to v
//			)
//			reviewResponse.add(ansMap)
//
//		}

		map["id"] = resident.id
		map["type"] = "logisticResident"
		map["data"] = mapOf(
				"basic" to basic,
				"questions" to questions
		)
		map["metadata"] = mapOf(
				"version" to resident.updateVersion + 1,
				KEY_CREATED_BY to mapOf(
						"userId" to resident.createdById,
						"utcDateTime" to resident.createDateTime
				),
				KEY_UPDATED_BY to resident.updates.map {
					mapOf(
							"userId" to it.userId,
							"utcDatessTime" to it.updateDateTime
					)
				}
		)

//		map["engagements"] = resident.engagements.map{
//			mapOf(
//					"answers" to it.questionURL,
//					"reviewDate" to it.reviewDate,
//					"reviewResponse" to it.reviewResponse
//			)
//		}

		return map
	}

	override fun unmarshal(properties: Map<String, Any>): Resident {

		val data = properties["data"] as Map<String, Any>
		val basic = data["basic"] as Map<String, Any>
		val address = basic["address"] as Map<String, String>
		val questionsList = data["questions"] as List<Map<String, Any>>
		val metadata = properties["metadata"] as Map<String, Any>
		val createdBy = metadata[KEY_CREATED_BY] as Map<String, String>
//		val engagement = properties["engagement"] as Map<String, Any>
//		val reviewResponseList = engagement["reviewResponse"] as  List<Map<String, Any>>

		return Resident().apply {
			id = properties["_id"] as String
			firstName = basic.getOrElse(KEY_FIRST_NAME, { "" }) as String
			middleName = basic.getOrElse(KEY_MIDDLE_NAME, { "" }) as String
			lastName = basic.getOrElse(KEY_LAST_NAME, { "" }) as String
			email = basic.getOrElse(KEY_EMAIL, { "" }) as String
			gender = basic[KEY_GENDER] as String
			maritalStatus = basic[KEY_MARITAL_STATUS] as String
			phone = basic[KEY_PHONE] as String
			dob = ZonedDateTime.parse(basic[LogisticResidentMapper.KEY_DATE_OF_BIRTH] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
			addressLine = address.getOrElse(KEY_ADDRESS_LINE, { "" })
			zipCode = address.getOrElse(KEY_ZIP_CODE, { "" })
			city = address.getOrElse(KEY_CITY, { "" })
			country = address.getOrElse(KEY_COUNTRY, { "" })
			isoCountry = address.getOrElse(KEY_ISO_COUNTRY, { "" })
			this.questions = questionsList.associate {
				it["questionCode"] as String to it["answer"]
			} as LinkedHashMap<String, Any>
//			this.reviewResponse = reviewResponseList.associate {
//				it["questionCode"] as String to it["answer"]
//			} as LinkedHashMap<String, Any>

			updateVersion = metadata.getOrElse("version", { 1 }) as Int
			createdById = createdBy.getOrElse("userId", { null })
			createDateTime = createdBy.getOrElse("utcDateTime") { null } as String

			if (metadata.containsKey(KEY_UPDATED_BY)) {
				updates = ArrayList()
				val upds = metadata[KEY_UPDATED_BY] as List<Map<String, String>>
				for (upd in upds) {
					updates.add(
							Resident.Update(
									upd.getOrElse("userId") { "" },
									upd.getOrElse("utcDateTime") { "" }
							)
					)
				}
			}

//			engagements = ArrayList()
//			val engagement = properties["engagements"] as List<Map<String, String>>
//			for (engage in engagement){
//				engagements.add(
//						Resident.Engagement(
//								engage.getOrElse("reviewDate", {" "}),
//								engage.getOrElse("questionURL", {" "})
//						)
//				)
//			}
		}
	}
}