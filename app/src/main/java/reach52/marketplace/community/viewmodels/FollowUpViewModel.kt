package reach52.marketplace.community.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.follow_up.FollowUp
import reach52.marketplace.community.models.logistic_resident.LogisticResident

class FollowUpViewModel : ViewModel() {
    var selectedLogisticResident : MutableLiveData<LogisticResident> = MutableLiveData()
    var selectedResident : MutableLiveData<Resident> = MutableLiveData()
    var selectedFollowId: MutableLiveData<String> = MutableLiveData()
    var isLogisticResident : MutableLiveData<Boolean> = MutableLiveData()
    var isAllFollowUpList: MutableLiveData<Boolean> = MutableLiveData()
    var selectedFollowUp : MutableLiveData<FollowUp> = MutableLiveData()

    fun resetFollowUpVM() {
        isLogisticResident = MutableLiveData()
        selectedLogisticResident = MutableLiveData()
        selectedResident = MutableLiveData()
        isAllFollowUpList = MutableLiveData()
        selectedFollowUp = MutableLiveData()
    }

}