package reach52.marketplace.community.signup

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.DispergoApp
import reach52.marketplace.community.auth.entity.RegistrationUser
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.auth.repo.PINRepo
import reach52.marketplace.community.extensions.utils.CountryManager
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.extensions.utils.calculateAgeFromDOB
import reach52.marketplace.community.extensions.utils.isPhoneInvalid
import reach52.marketplace.community.persistence.Api
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


class RegistrationHelper {

//	private val authKey = "8a673ab6-d87e-40af-be58-805659f8ca26"
	private val authKey = "8a5812eb-f9e2-4211-9bc6-b1221354b297"
	private val countryCode = CountryManager.getCountryCode()
	private val countryCodeCount = CountryManager().getCountryPhoneCode(countryCode).length

	fun registerNewUser(context: Context, user: RegistrationUser) = Completable.create {

		val validationError = validateUserInput(user)

		if (validationError != null) {
			it.onError(Exception(validationError))
			return@create
		}

		val pin = PINRepo.getPIN(context)

		val stringData = mapOf(
				"username" to user.email,
				"birthdate" to user.DOB,
				"password" to pin,
				"roles" to user.role,
				"first_name" to user.firstName,
				"last_name" to user.lastName,
				"country" to user.country,
				"mobile_number" to user.phone,
				"town" to user.town,
				"region" to user.region
		)

		val files = mapOf(
				"photo" to user.profilePic
		)

		val token = LocalUserRepo.getToken(context)
		val headers = mapOf("Authorization" to token)

		NetworkManager.makeMultipartRequest(
				NetworkManager.POST,
				Api.registration,
				stringData,
				files,
				headers,
				context
		).subscribe({ response ->

			if (response.isSuccessfull) {
				val success = response.json.getBoolean("success")
				if (success) {

					it.onComplete()

				} else {

					val msg = response.json.getString("msg")
					it.onError(ResponseException(msg, Api.registration, response))
				}
			}


		}, { error ->
			it.onError(error)
		})


	}.doOnError {

		FirebaseCrashlytics.getInstance().setCustomKey("email", user.email)
		if (it is ResponseException) {
			FirebaseCrashlytics.getInstance().setCustomKey("api", it.api)
			FirebaseCrashlytics.getInstance().setCustomKey("response json", it.response.json.toString())
		}
		FirebaseCrashlytics.getInstance().recordException(it)

	}.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())

//	private fun saveResponseData(response: NetworkManager.SimpleResponse, context: Context = DispergoApp.get()) {
//		Auth.saveSession(response.json.toString(), context)
//		val id = response.json.getJSONObject("user").getString("_id")
//		SharedPrefs.write(SharedPrefs.KEY_ID, id, context)
//	}

	fun uploadPhotoID(photo: File, context: Context = DispergoApp.get()) = Completable.create {

		val token = LocalUserRepo.getToken(context)

		NetworkManager.makeMultipartRequest(
				NetworkManager.PUT,
				Api.profilePhotoUrl,
				null,
				mapOf("documentId" to photo),
				mapOf("Authorization" to token),
				context)
				.subscribe(
						{ response ->

							if (response.isSuccessfull) {

								val token = response.json["token"] as String
								PINRepo.updatePINReset(false)
								LocalUserRepo.reset()
								LocalUserRepo.saveData(context, token)

								it.onComplete()
							} else {
								it.onError(IDUploadFailException())
							}

						},
						{ error ->


							if (error.message == "Image size is not acceptable") {
								it.onError(ImageTooLargeException())
							} else {
								it.onError(error)
							}

						})

	}.doOnError {

//		val id = SharedPrefs.readString(SharedPrefs.KEY_ID)
//		FirebaseCrashlytics.getInstance().setCustomKey("id", id!!)

	}
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())

	private fun validateUserInput(user: RegistrationUser): String? {

		if (user.DOB.isNullOrEmpty()) {
			return "Please set birth date"
		}

		val age = calculateAgeFromDOB(user.DOB!!)
		if (age < 18) {
			return "You must be at least 18 year to continue."
		}
		if (user.firstName.isNullOrEmpty()) {
			return "First name cannot be empty"
		}
		if (user.lastName.isNullOrEmpty()) {
			return "Last cannot be empty"
		}
		if (user.phone!!.removeRange(0, countryCodeCount) != "") {
			if (user.phone.toString().length < 10) {
				return "Please enter valid phone number"
			}

			if (user.phone.toString().length >= 15) {
				return "Phone number must be 15 characters or not less than 10"
			}

			if (isPhoneInvalid(user.phone.toString())) {
				return "Invalid phone number"
			}

		} else {
			return "Please enter phone number"
		}

		if (user.gender.isNullOrEmpty()) {
			return "Gender not set"
		}

		if (user.profilePic == null) {
			return "Profile picture not set"
		}

		return null
	}

	class ImageTooLargeException : Exception()

	class IDUploadFailException : Exception()

	class ResponseException(message: String, val api: String, val response: NetworkManager.SimpleResponse) : Exception(message)

}


