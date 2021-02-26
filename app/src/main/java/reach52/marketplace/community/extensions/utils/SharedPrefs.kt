package reach52.marketplace.community.extensions.utils

import android.content.Context
import reach52.marketplace.community.DispergoApp

/**
 * Helper class for SharedPreference operations and keys.
 */
object SharedPrefs {

	const val KEY_PIN = "pn"
	const val KEY_LANGUAGE = "lang"
	const val KEY_ROLE = "role"
	const val KEY_ID = "id"
	const val KEY_COUNTRY = "country_iso"
	const val KEY_USER_TOKEN = "udtk" // Token contains User Data
	const val KEY_PIN_RESET = "pnrt"

	fun write(key: String, value: String, context: Context = DispergoApp.get()) = getPrefs(context).edit().putString(key, value).commit()

	fun write(key: String, value: Int, context: Context = DispergoApp.get()) = getPrefs(context).edit().putInt(key, value).commit()

	fun write(key: String, value: Boolean, context: Context = DispergoApp.get()) = getPrefs(context).edit().putBoolean(key, value).commit()

	fun readString(key: String, def: String? = null, context: Context = DispergoApp.get()) = getPrefs(context).getString(key, def)

	fun readBoolean(key: String, def: Boolean = false, context: Context = DispergoApp.get()) = getPrefs(context).getBoolean(key, def)

	fun readInt(key: String, def: Int = -1, context: Context = DispergoApp.get()) = getPrefs(context).getInt(key, def)

	fun delete(key: String, context: Context = DispergoApp.get()) = getPrefs(context).edit().remove(key).commit()

	private fun getPrefs(context: Context) = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
}