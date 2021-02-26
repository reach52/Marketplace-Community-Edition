package reach52.marketplace.community.resident.entity

import reach52.marketplace.community.extensions.utils.calculateAgeFromDOB
import reach52.marketplace.community.models.logistic_resident.CreatedBy
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class Resident {

	lateinit var id: String
	lateinit var firstName: String
	var middleName: String? = null
	lateinit var lastName: String
	lateinit var phone: String
	var email: String? = null
	lateinit var dob: ZonedDateTime
	lateinit var addressLine: String
	lateinit var city: String
	lateinit var zipCode: String
	lateinit var isoCountry: String
	lateinit var country: String
	lateinit var gender: String
	lateinit var maritalStatus: String

	var createdById: String? = null
	var createDateTime: String? = null
	var updates = ArrayList<Update>()
	var engagements = ArrayList<Engagement>()
	var updateVersion = 0

	fun fullName() : String {
		//return "$lastName, $firstName"
		return "$firstName, $lastName"

	}

	fun isDobInitialized() = ::dob.isInitialized

	fun getAgeString() = getAge().toString()
	fun getAge() = calculateAgeFromDOB(dob)

	// Tempopary function to maintain compatibiltiy
	fun toLogisticResident(): LogisticResident {

		return LogisticResident(
				id,
				"",
				"",
				firstName,
				lastName,
				middleName!!,
				email!!,
				gender,
				maritalStatus,
				phone,
				dob,
				addressLine,
				"",
				zipCode,
				city,
				country,
				CreatedBy(ZonedDateTime.now(ZoneOffset.UTC), "", "", ""),
				updates.map {
					CreatedBy(ZonedDateTime.now(ZoneOffset.UTC), "", it.userId, "")
				}
		)

	}

	var questions = LinkedHashMap<String, Any>()
	lateinit var answers: Map<String, String>


	data class Option(
			val index: Int,
			val code: String,
			val text: String
	)

	data class Update(
			val userId: String,
			val updateDateTime: String
	)

	data class Engagement(
			val reviewDate: String,
			val questionURL: String? = null,
			var reviewResponse: Map<String, List<String>>
	)

//	data class ReviewResponse(
//			val answers: Map<String, String>,
//			val questionCode: String
//	)
}