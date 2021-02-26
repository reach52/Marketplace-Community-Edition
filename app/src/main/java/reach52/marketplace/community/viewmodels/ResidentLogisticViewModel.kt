package reach52.marketplace.community.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.models.logistic_resident.LogisticResident

class ResidentLogisticViewModel : ViewModel() {
    var selectedLogisticResident: MutableLiveData<LogisticResident> = MutableLiveData()
    var isLogisticResident: MutableLiveData<Boolean> = MutableLiveData()
    var isLogisticResidentUpdate: MutableLiveData<Boolean> = MutableLiveData()
    var residentCounter: MutableLiveData<Int> = MutableLiveData()

    fun resetResidentVM() {
        isLogisticResidentUpdate = MutableLiveData()
        selectedLogisticResident = MutableLiveData()
    }
}