package reach52.marketplace.community.data


import reach52.marketplace.community.app.AppUtils
import reach52.marketplace.community.extensions.utils.CountryCode
import java.util.*

data class LoginRequestModel(
    val username: String,
    val password: String,
    val lastLoginTimestamp: Date = AppUtils.currentDateFormat(AppUtils.getCurrentDateTime()),
    val appVersion: String = "1",
    val isoCountry: String = CountryCode.IND
)