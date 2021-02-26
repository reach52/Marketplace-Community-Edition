package reach52.marketplace.community.auth.entity

import java.io.File

class RegistrationUser(val email: String) {

	var role = ROLE_SELF_SIGNPUP
	var firstName: String? = null
	var lastName: String? = null
	var gender: String? = null
	var DOB: String? = null
	var phone: String? = null
	var country: String? = null
	var region: String? = null
	var town: String? = null
	var profilePic: File? = null

	companion object{
		const val ROLE_SELF_SIGNPUP = "self_signup_mam"
		const val ROLE_MAM = "mam"
	}

}