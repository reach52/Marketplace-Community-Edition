package reach52.marketplace.community.persistence

import reach52.marketplace.community.extensions.utils.calculateAgeFromDOB
import reach52.marketplace.community.models.Dependent
import io.reactivex.Single

object DependentManager {

	fun validateDependent(dependent: Dependent) = Single.create<Boolean> {

		if (dependent.fullName.isEmpty()) {
			it.onError(EmptyNameException())
			it.onSuccess(false)
		} else {
			try {
				val age = calculateAgeFromDOB(dependent.DOB)
				if (age < 18) {
					it.onError(AgeTooLowException())
					it.onSuccess(false)
				} else {
					it.onSuccess(true)
				}
			} catch (e: NumberFormatException) {
				it.onError(e)
				it.onSuccess(false)
			}

		}

	}

	class EmptyNameException : Exception()

	class AgeTooLowException : Exception()

}