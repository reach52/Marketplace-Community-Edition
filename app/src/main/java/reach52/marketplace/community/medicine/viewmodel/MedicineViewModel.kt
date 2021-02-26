package reach52.marketplace.community.medicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.medicine.repo.MedicineRepo
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class MedicineViewModel : ViewModel(){
    val medicines = MutableLiveData<ArrayList<Medicine>>()
    private var totalMedicines = ArrayList<Medicine>()

    var suplCodeList = ArrayList<String>()

    fun loadMedicines(context: Context, supplierList: List<String>) {

//        if (totalMedicines.isEmpty()) {
            MedicineRepo.getAllMedicines(context, supplierList).subscribe({
                medicines.value?.clear()
	            Log.i("taaag","hello: $it")
                medicines.value = it
                totalMedicines = it
            }, {
                Log.d("taaaag", it.toString())
            })
//        } else {
//            medicines.value = totalMedicines
//        }
    }

	fun searchFilterMedicines(context: Context, supplierCodeList: ArrayList<String>, searchStr: String) {

		if (searchStr.isEmpty()) {
			Log.i("taaag", "loading medicines from search")
			loadMedicines(context, supplierCodeList)
		} else {

			Single.create<ArrayList<Medicine>> { emitter ->
				val searchedList = ArrayList<Medicine>()
				for (it in totalMedicines) {
					val fields = listOf(
							it.brandName,
							it.genericName,
							it.manufacturer
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
						medicines.value = it
					}, {

					})

		}

	}

}