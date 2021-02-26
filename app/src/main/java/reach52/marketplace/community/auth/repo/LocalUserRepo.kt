package reach52.marketplace.community.auth.repo

import android.content.Context
import android.util.Log
import reach52.marketplace.community.auth.entity.Document
import reach52.marketplace.community.auth.entity.LocalUser
import reach52.marketplace.community.auth.utils.EncryptionUtils
import reach52.marketplace.community.auth.utils.JWTUtils
import reach52.marketplace.community.extensions.utils.SharedPrefs

object LocalUserRepo {

	private val KEY = ""

	private var localUser: LocalUser? = null

	fun userDataExistsLocally() = SharedPrefs.readString(SharedPrefs.KEY_USER_TOKEN, null) != null

	fun saveData(context: Context, token: String) {

		val encData = EncryptionUtils.encrypt(token, KEY)

		SharedPrefs.write(SharedPrefs.KEY_USER_TOKEN, encData, context)
		Log.v("taaag", "token saved: $encData")

	}

	fun getUser(context: Context): LocalUser {

		if (localUser == null) {

			val token = getToken(context)

			val data = JWTUtils.decode(token, "user")

			localUser = LocalUser(
					data["_id"] as String,
					data["username"] as String,
					data["personalUsername"] as String,
					data["first_name"] as String,
					data["last_name"] as String,
					data["country"] as String
			)

			if(data.containsKey("documentsId")) {
				val docs = (data["documentsId"] as ArrayList<Map<*, *>>)

				for (doc in docs) {
					localUser!!.documents += Document(
							doc["name"] as String,
							doc["photo"] as String
					)
				}
			}

			try {
				val tags = (data["marketPlace"] as Map<String, List<String>>)["catalogTags"]
				for (tag in tags!!) {
					localUser!!.catalogTags += tag
				}
			} catch (e: Exception) {

			}


		}

		return localUser!!

	}

	fun getUserEmail(context: Context) : String{

		val token = getToken(context)
		val data = JWTUtils.decode(token, "user")
		val email = data["username"] as String
		return email

	}

	fun getToken(context: Context): String {
		val encToken = SharedPrefs.readString(SharedPrefs.KEY_USER_TOKEN, null, context)!!
		val token = EncryptionUtils.decrypt(encToken, KEY)
		return token
	}

	fun deleteData(context: Context) {
		SharedPrefs.delete(SharedPrefs.KEY_USER_TOKEN, context)
		Log.d("taaag", "token deleted")
	}

	fun reset(){
		localUser = null
	}

}