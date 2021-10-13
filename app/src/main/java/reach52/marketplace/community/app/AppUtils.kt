package reach52.marketplace.community.app

import com.google.gson.Gson
import com.reach52.healthcare.util.SecurePreferences
import reach52.marketplace.community.data.LoginResponseModel
import reach52.marketplace.community.util.Constant
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun currentDateFormat(curDate: String): Date {
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val myDate: Date = dateFormat.parse(curDate)
        return myDate
    }

    fun getCurrentDateTime(): String {
        val todaysdate = Date()
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val date = format.format(todaysdate)
        /* val current = LocalDateTime.now()
         val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzzz yyyy")
         val formatted = current.format(formatter)*/
        return date.toString()
    }

    @JvmStatic
    fun getLoginedUser(): LoginResponseModel.Data? {
        var loggedInUser: LoginResponseModel.Data? = null
        val preferences =
            SecurePreferences(
                DispergoApp.getAppContext()!!,
                Constant.preferenceName,
                Constant.secureKey,
                true
            )
        val gson = Gson()

        if (preferences != null && preferences.containsKey(Constant.USER_DETAILS)) {
            val json: String = preferences.getString(Constant.USER_DETAILS)!!
            return gson.fromJson(json, LoginResponseModel.Data::class.java)
        }
        return loggedInUser

    }
}