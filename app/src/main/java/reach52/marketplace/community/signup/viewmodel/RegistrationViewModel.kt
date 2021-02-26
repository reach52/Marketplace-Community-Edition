package reach52.marketplace.community.signup.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.auth.entity.RegistrationUser
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.signup.RegistrationHelper

class RegistrationViewModel : ViewModel() {

	lateinit var user: RegistrationUser
	private val registration = RegistrationHelper()

	fun init(context: Context){

		val email = LocalUserRepo.getUserEmail(context)
		user = RegistrationUser(email)

	}

	fun register(context: Context) = registration.registerNewUser(context, user)

}