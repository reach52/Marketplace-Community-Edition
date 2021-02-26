package reach52.marketplace.community.medicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.medicine.entity.Supplier
import reach52.marketplace.community.medicine.repo.SupplierRepo
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.repo.ResidentRepo
import io.reactivex.Completable

@SuppressLint("CheckResult")
class MedicationOrderViewModel:  ViewModel() {

    lateinit var resident: Resident
    val suppliers = MutableLiveData<ArrayList<Supplier>>()

    fun loadSuppliers(context: Context){
        SupplierRepo.getAllSuppliers(context).subscribe(
                {
                    suppliers.value = it
                },
                {

                }
        )
    }

    fun loadResident(context: Context, id: String) = Completable.create {

        ResidentRepo.fetchResident(context, id).subscribe(
                { r ->
                    resident = r
                    it.onComplete()
                },
                {

                }
        )
    }
}