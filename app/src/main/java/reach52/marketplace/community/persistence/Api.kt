package reach52.marketplace.community.persistence

import reach52.marketplace.community.BuildConfig

object Api {

	private const val flavour = BuildConfig.FLAVOR
//	private const val flavour = "uat" // Temporarily set to uat. Revert later

	private val host = when (flavour) {
		//Auth API should be declared here
		"live" -> ""
		"demo" -> ""
		else -> ""
	}

	val login = "$host/v2/account/login"
	val registration = "$host/v2/account/register"
	val accountSetup = "$host/v2/account/setup"
	val pinRest = "$host/v2/account/reset/pin"

	val profilePhotoUrl = "$host/v2/account/photo/upload"

	val imageUploadUrl = ""

	val forceUpdateUrl = ""

	val reEngagementQuestionUrl = ""

}