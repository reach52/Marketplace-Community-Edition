package reach52.marketplace.community.medicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.medicine.entity.Supplier
import reach52.marketplace.community.medicine.repo.SupplierRepo

class SupplierViewModel : ViewModel(){

    val suppliers = MutableLiveData<ArrayList<Supplier>>()

    @SuppressLint("CheckResult")
    fun loadSuppliers(context: Context) {
        SupplierRepo.getAllSuppliers(context).subscribe({
            suppliers.value = it
        },{
            Log.d("taaaag", it.toString())
        })
    }
}