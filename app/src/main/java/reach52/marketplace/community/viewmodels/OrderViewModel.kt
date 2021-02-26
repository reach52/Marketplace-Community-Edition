package reach52.marketplace.community.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.None
import arrow.core.Option
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.medicine.entity.Supplier
import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.models.medication.Physician
import reach52.marketplace.community.models.medication.Suppliers
import io.reactivex.Observable

class OrderViewModel : ViewModel() {
    val discounts: Observable<Discount> = Observable.fromArray(
            Discount("PH-PWD", 0.20, 0.12),
            Discount("PH-SENIOR", 0.20, 0.12)
    )

    var discountIdNumber: Option<String> = None
    var photoPath: Option<String> = None
    var hcpNumber:Option<String> = None
    var prescriptionNumber: Option<String> = None
    var selectedDiscount: Option<Discount> = None
    var selectedPhysician: Option<Physician> = None
    var selectedResident: Option<Resident> = None
    var selectedLogisticResident: Option<LogisticResident> = None
    var selectedSuppliers: MutableLiveData<Suppliers> = MutableLiveData()
    var selectedSupplier: MutableLiveData<Supplier> = MutableLiveData()
    var selectedMedication: MutableLiveData<Medicine> = MutableLiveData()
    var selectMedication: Option<Medicine> = None

    fun reset() {
        discountIdNumber = None
        photoPath = None
        hcpNumber = None
        prescriptionNumber = None
        selectedDiscount = None
        selectedPhysician = None
    }
}

