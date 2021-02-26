package reach52.marketplace.community.auth

import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.auth.entity.AccountFlags
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.auth.repo.PINRepo
import reach52.marketplace.community.auth.repo.PINRepo.PIN_LENGTH
import reach52.marketplace.community.auth.utils.JWTUtils
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.persistence.Api
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.ResidentDatabase
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object Auth {

	const val LOGIN_ACCOUNT_SETUP = 1
	const val LOGIN_PIN_RESET = 2
	const val LOGIN_REGISTRATION = 3
	const val LOGIN_SUCCESS = 4

	fun login(uid: String, pin: String, context: Context) = Single.create<Int> {

		if (uid.isEmpty()) {
			it.onError(InvalidUIDException())
			return@create
		}

		if (pin.isEmpty()) {
			it.onError(PINRepo.InvalidPINException())
			return@create
		}

		if (pin.length < PIN_LENGTH) {
			it.onError(PINRepo.PINTooShortException())
			return@create
		}

		val hashedPIN = PINRepo.hash(pin)

		Log.d("taaag", "email: $uid, pin: $pin, hashedPIN: $hashedPIN")
		val body = JSONObject()
				.put("username", uid)
				.put("password", pin)
				.put("hashed", false)
				.put("appId", "dispergo-android")

		NetworkManager.makePOSTRequest(
				Api.login,
				body,
				null,
				context
		).subscribe(
				{ response ->
					val token = response.json["token"] as String
					PINRepo.updatePINReset(false)
					LocalUserRepo.reset()
					LocalUserRepo.saveData(context, token)

					val flags = extractAccountFlags(token)

					if (!flags.isAccountSetup) {
						it.onSuccess(LOGIN_ACCOUNT_SETUP)
						return@subscribe
					}

					if (flags.pinReset) {

						it.onSuccess(LOGIN_PIN_RESET)
						return@subscribe
					}

					if (!flags.isRegistrationDone) {

						it.onSuccess(LOGIN_REGISTRATION)
						return@subscribe
					}

					PINRepo.savePIN(context, hashedPIN)
					it.onSuccess(LOGIN_SUCCESS)
					return@subscribe

				},
				{ err ->
					it.onError(err)
				}
		)

	}.doOnError {
		FirebaseCrashlytics.getInstance().setCustomKey("email/username", uid)
		FirebaseCrashlytics.getInstance().recordException(it)
	}

	fun accountSetup(context: Context, username: String, pin: String) = Completable.create {

		if (username.isEmpty()) {
			it.onError(InvalidUsernameException())
			return@create
		}

		if (username.length < 6) {
			it.onError(UsernameTooShortException())
			return@create
		}

		if (pin.isEmpty()) {
			it.onError(PINRepo.InvalidPINException())
			return@create
		}

		if (pin.length < PIN_LENGTH) {
			it.onError(PINRepo.PINTooShortException())
			return@create
		}

		val body = JSONObject()
				.put("username", username)
				.put("password", pin)

		val token = LocalUserRepo.getToken(context)

		NetworkManager.makePUTRequest(
				Api.accountSetup,
				body,
				mapOf("Authorization" to token),
				context
		).subscribe(
				{ _ ->
					it.onComplete()
				},
				{ err ->
					it.onError(err)
				}
		)

	}

	fun logout(context: Context) {
		LocalUserRepo.reset()
		LocalUserRepo.deleteData(context)
		PINRepo.deletePIN(context)
		PINRepo.updatePINReset(true)

		context.cacheDir.deleteRecursively()

		DispergoDatabase.resetDatabase()
		// reset singleton for resident local database
		ResidentDatabase.resetResidentDB()

	}

	fun checkIfPhotoIDUploaded(context: Context): Boolean {

		val user = LocalUserRepo.getUser(context)

//		it.onSuccess(user.documents.size != 0)
		return user.documents.size != 0

	}

	private fun extractAccountFlags(token: String): AccountFlags {

		val data = JWTUtils.decode(token, "user")

		val flags = AccountFlags()

		if (data.contains("isAccountSetUp")) {
			flags.isAccountSetup = data["isAccountSetUp"] as Boolean
		}

		if (data.contains("pinReset")) {
			flags.pinReset = data["pinReset"] as Boolean
		}

		if (data.contains("isNotRegistered")) {
			flags.isRegistrationDone = !(data["isNotRegistered"] as Boolean)
		}

		return flags

	}

	class InvalidUIDException : Exception()
	class UsernameTooShortException : Exception()
	class InvalidUsernameException : Exception()


}
