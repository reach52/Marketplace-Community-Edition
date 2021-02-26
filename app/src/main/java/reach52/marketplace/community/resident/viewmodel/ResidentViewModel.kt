package reach52.marketplace.community.resident.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.insurance.entity.PolicyOrder
import reach52.marketplace.community.insurance.repo.OrderRepo
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.repo.ResidentRepo
import io.reactivex.Completable
import io.reactivex.Single

@SuppressLint("CheckResult")
class ResidentViewModel : ViewModel() {

	var update = false
	var isLogisticResident = true
	lateinit var residentId: String
	var resident = Resident()

	val policyOrders = MutableLiveData<ArrayList<PolicyOrder>>()

	fun loadResidentForId(context: Context, id: String) = Single.create<Resident> {
		residentId = id
		ResidentRepo.fetchResident(context, id).subscribe({ resi ->

			resident = resi
			it.onSuccess(resident)

		}, {
			it.printStackTrace()
		})

	}

	fun updateResident(context: Context) = Completable.create {
		Log.d("irish", "test")

		ResidentRepo.updateLogisticResident(context, resident).subscribe({
			it.onComplete()
		}, { err ->
			it.onError(err)
		})

	}

	fun saveNewResident(context: Context) = Completable.create {
		ResidentRepo.createNewLogisticResident(context, resident)
		it.onComplete()
	}

	fun loadPolicyOrdersForResident(context: Context, id: String) {

		OrderRepo.getPolicyOrders(context, id).subscribe(
				{
					policyOrders.value = it
				},
				{

				}
		)


	}

	fun reset() {
		resident = Resident()
	}

//    fun saveEngagement(context: Context, answers: Map<String, String>) {
//
//    	ResidentRepo.updateResidentEngagement(context, resident, answers)
//
//    }

	fun saveEngagement(context: Context, answers: Map<String, String>) = Completable.create {
		Log.d("irish", "test")
		ResidentRepo.updateResidentEngagement(context, resident, "https://auth-uat.reach52.com/v2/general/questions", answers).subscribe({
			it.onComplete()
		}, { err ->
			it.onError(err)
		})
	}




}