package reach52.marketplace.community.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityViewModel : ViewModel() {

	val email = MutableLiveData<String>().apply {
		value = ""
	}
	var firstName: String = ""
	var lastName: String = ""

	fun resetData() {
		email.value = ""
		firstName = ""
		lastName = ""
	}

}