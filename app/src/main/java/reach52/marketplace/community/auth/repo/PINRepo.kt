package reach52.marketplace.community.auth.repo

import android.content.Context
import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import reach52.marketplace.community.extensions.utils.SharedPrefs
import io.reactivex.Completable

object PINRepo {

	const val PIN_LENGTH = 4

	fun verifyPIN(context: Context, inputPIN: String): Boolean {

		if (inputPIN.length != PIN_LENGTH) {
			throw PINTooShortException()
		}

		val savedPIN = getPIN(context) ?: throw InvalidPINException()

		return BCrypt.verifyer().verify(inputPIN.toCharArray(), savedPIN.toCharArray()).verified

	}

	fun savePIN(context: Context, pin: String) {

		SharedPrefs.write(SharedPrefs.KEY_PIN, pin, context)
		Log.d("taaag", "pin saved: $pin")

	}

	fun getPIN(context: Context): String? {
		return SharedPrefs.readString(SharedPrefs.KEY_PIN, context = context)
	}

	fun deletePIN(context: Context) {
		SharedPrefs.delete(SharedPrefs.KEY_PIN, context)
		Log.d("taaag", "pin deleted")
	}

	fun isPINResetSet() = SharedPrefs.readBoolean(SharedPrefs.KEY_PIN_RESET, true)

	fun updatePINReset(reset: Boolean) {
		SharedPrefs.write(SharedPrefs.KEY_PIN_RESET, reset)
	}

	fun requestPINReset(context: Context, newPIN: String) = Completable.create {

		if (newPIN.length < PIN_LENGTH) {
			it.onError(PINTooShortException())
			return@create
		}

		it.onComplete()

	}

	fun hash(pin: String): String = BCrypt.withDefaults().hashToString(6, pin.toCharArray())

	class InvalidPINException : Exception()
	class PINTooShortException : Exception()

}