package reach52.marketplace.community.resident.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.repo.ResidentRepo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class ResidentsViewModel : ViewModel() {

	val logisticResidents = MutableLiveData<ArrayList<Resident>>()
	private var totalResidents = ArrayList<Resident>()

	fun loadLogisticsResidents(context: Context) {

		ResidentRepo.getLogisticResidents(context).subscribe({ res ->

			logisticResidents.value = res
			totalResidents = res


		}, {
			it.printStackTrace()
		})

	}

	fun searchFilterLogisticsResidents(context: Context, searchStr: String) {

		if (searchStr.isEmpty()) {
			Log.i("taaag", "loading residents from search")
			loadLogisticsResidents(context)
		} else {

			Single.create<ArrayList<Resident>> { emitter ->
				val searchedList = ArrayList<Resident>()
				for(it in totalResidents) {
					val fields = listOf(
							it.firstName,
							it.lastName,
							it.city,
							it.addressLine,
							it.phone,
							it.zipCode
					)
					for (field in fields) {
						if (field.toLowerCase().contains(searchStr.toLowerCase())) {
							searchedList += it
							break
						}
					}

				}
				emitter.onSuccess(searchedList)
			}
					.subscribeOn(Schedulers.computation())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe({
						logisticResidents.value = it
					}, {

					})

		}

	}

}