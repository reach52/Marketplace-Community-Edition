package reach52.marketplace.community.medicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.DispergoApp
import reach52.marketplace.community.R
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.medicine.entity.*
import reach52.marketplace.community.medicine.repo.DiscountRepo
import reach52.marketplace.community.medicine.repo.OrderRepo
import reach52.marketplace.community.medicine.repo.PhysicianRepo
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.repo.ResidentRepo
import io.reactivex.Completable
import java.io.File

@SuppressLint("CheckResult")
class MedicationPurchaseViewModel : ViewModel() {

    var selectedPhysician: Physician? = null
    lateinit var resident: Resident

    val physicians = MutableLiveData<ArrayList<Physician>>()
    val discounts = MutableLiveData<ArrayList<Discount>>()
    var prescriptionNumber = ""
    var discountNumber = ""
    var selectedDiscount: Discount? = null
    val prescriptionImageFile by lazy { File(DispergoApp.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "prescription.png") }

    var currency: String = ""
    var subTotal = 0.0
    var deliveryFee = 0.0
    var taxAmt = 0.0
    var discountAmt = 0.0
    var total = 0.0

    var selectedmultiSupplier: ArrayList<Supplier> = ArrayList<Supplier>()
    val selectedMedicines = MutableLiveData<ArrayList<SelectedMedicine>>()
    var items: ArrayList<MedicinePurchase.Item> = ArrayList()
    val selectedMedCount = MutableLiveData<Int>()

    val cartcount = 0

    init {
        if (prescriptionImageFile.exists()) {
            prescriptionImageFile.delete()
        }
        selectedMedicines.value = ArrayList()
//		selectedDiscount = DiscountRepo.getTestDiscount()

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

    fun loadPhysicians(context: Context) {
        PhysicianRepo.getAllPhysician(context).subscribe({
            it.add(0, Physician("", "", Physician.Name("No selection", "", ""), ""))
            physicians.value = it
        }, {
            Log.d("taaaag", it.toString())
        })
    }

    fun loadDiscounts(context: Context) {
        DiscountRepo.fetchDiscounts(context).subscribe(
                {
                    it.add(0, Discount("", context.getString(R.string.none).toUpperCase(), "No discount", 0.0, false))
                    discounts.value = it
                },
                {
                    it.printStackTrace()
                }
        )
    }
    fun calculatePricing() {
        // added the size in  selectedMedCount for show on counter
        Log.d("taaag", "calculatePricing: selected medicine size " + selectedMedicines.value!!.size)
        subTotal = 0.0
        taxAmt = 0.0

        selectedMedicines.value!!.forEach {
            subTotal += it.qty * it.medicine.price
            taxAmt += calculateTax(it)
        }

		subTotal = String.format("%.2f", subTotal).toDouble()

		if (selectedMedicines.value!!.isEmpty()) {
			deliveryFee = 0.0
		} else {
			deliveryFee = selectedmultiSupplier.get(0).deliveryFee
		}

        if (selectedDiscount != null) {

            val value = selectedDiscount!!.value
            val isPer = selectedDiscount!!.isPercentage

            if (isPer) {
                discountAmt = (value * subTotal) / 100
            } else {
                discountAmt = value
            }

		}
		discountAmt = String.format("%.2f", discountAmt).toDouble()


        total = subTotal - discountAmt + taxAmt + deliveryFee
        total = String.format("%.2f", total).toDouble()

        Log.d("taaag", "sub total price: $subTotal")
        Log.i("taaag", "Total amount: $total")
    }

    private fun calculateTax(medicine: SelectedMedicine): Double{

        val tax = medicine.medicine.tax
        val price = medicine.medicine.price * medicine.qty
        var taxAmt = if (tax.isIncluded) {
            0.0
        } else {
            val taxPercentage = tax.percentage
            (taxPercentage * price) / 100
        }
        taxAmt = String.format("%.2f", taxAmt).toDouble()
        return taxAmt
    }


    fun addMedicine(medicine: Medicine ,qty:Int) {

        if (currency.isEmpty()) {
            currency = medicine.isoCurrency
        }

        val med = selectedMedicines.value!!.firstOrNull {
            medicine.id == it.medicine.id
        }

        if (med == null) {
            val selectedMed = SelectedMedicine(medicine,qty)
            selectedMedicines.value!!.add(selectedMed)
            selectedMedicines.value = selectedMedicines.value
        }

    }

    fun validatePrescription() {
		if (prescriptionNumber.isEmpty()) {
			throw PrescriptionNumberMissingException()
		}

        if (prescriptionImageFile.length() <= 0) {
            throw PrescriptionImageMissingException()
        }

    }

    fun validateDiscount(){

        if (selectedDiscount != null && discountNumber.isEmpty()) {
            throw DiscountNumberMissingException()
        }

    }

    fun saveOrder(context: Context): Completable {

        for (supp in selectedmultiSupplier.distinct()) {
            //	supp.r52SuppCode = supp.id+"_"+supp.supplierCode

            selectedMedicines.value!!.map {
                val m = it.medicine

                if (supp.supplierCode == it.medicine.supplierCode) {

                    val item = MedicinePurchase.Item(
                            m.id,
                            supp.supplierCode,
                            m.prescriptionRequired,
                            supp.supplierName,
                            supp.r52SuppCode,
                            m.information,
                            m.description,
                            m.brandName,
                            m.genericName,
                            m.dosage,
                            m.form,
                            listOf(), // TODO change this
                            m.packSize,
                            m.packUnit,
                            m.price,
                            m.tax,
                            it.qty,
                            it.qty,
                            total,
                            "requested",
                            ""
                    )
                    items.add(item)

                }
            }
        }

         /* if (selectedmultiSupplier.equals("demo")) {
              selectedmultiSupplier.remove(this)
          }*/

        // create purchase object
        val purchase = MedicinePurchase(
                selectedmultiSupplier[0].isoCountry,
                currency,
                resident,
                selectedPhysician,
                prescriptionImageFile.absolutePath,
                items,
                selectedmultiSupplier,
                deliveryFee,
                taxAmt,
                subTotal,
                total,
                prescriptionNumber,
                "",
                discountNumber,

                selectedDiscount,
                LocalUserRepo.getUser(context)
        )

        // use order repo to save it
        return OrderRepo.createNewOrder(context, purchase)

    }

    fun checkForPrescriptionRequired() : Boolean{

        for (med in selectedMedicines.value!!) {

            if (med.medicine.prescriptionRequired) {
                return true
            }

        }

        if (prescriptionImageFile.exists()) {
            prescriptionImageFile.delete()
        }
        discountNumber = ""
        selectedDiscount = null


        return false

    }

    //    fun updateSubOrderPaymentStatusOnline(str: String) =
//        if (str == "COD") {
//            repository?.updateSubOrderPaymentStatusOnline(
//                purchaseId,
//                "UNPAID",
//                "COD"
//            )
//        } else {
//            repository?.updateSubOrderPaymentStatusOnline(
//                purchaseId,
//                "UNPAID",
//                "Online"
//            )
//        }
    var purchasedId: String? = null
    var shouldSendEmail: Boolean = true
    var payerEmail: String? = null

    var _paymentMutable = MutableLiveData<PaymentResponseModel>()
    val paymentMutable: LiveData<PaymentResponseModel>
        get() = _paymentMutable

    fun paymentInvoiceApi() {
        _paymentMutable = MutableLiveData()

        val loginService = reach52.marketplace.community.app.DispergoApp.create3()
//        val call = getLoginedUser()!!._id.let {
//            getLoginedUser()?.email?.let { it1 ->
//                PaymentRequestModel(
//                    purchaseId,
//                    it,
////                    realmResident._id.toString(),
////                    "sasasasa",
//                    residentId.id,
//                    total.toString(),
//                    "description",
//                    it1
//                )
//            }
//        }?.let {
//            loginService.getPaymentInvoiceUrl(
//                it
//            )
//        }

        val call = resident.id.let {
            resident.email.let {
//                Log.d("pay", meds)
                PaymentRequestModel(
                    "",
                    total,
                    "descriptions",
                    shouldSendEmail,
                    "email@reach52.com",
                    PaymentRequestModel.Customers(resident.firstName, "email@reach52.com", resident.phone),
                    "PHP",
                    listOf(PaymentRequestModel.Items("item name", 2, 100))
                )
            }
        }?.let {
            loginService.getPaymentInvoiceUrl(
                it
            )
        }
        call!!.enqueue(object : retrofit2.Callback<PaymentResponseModel?> {
            override fun onResponse(
                call: retrofit2.Call<PaymentResponseModel?>,
                response: retrofit2.Response <PaymentResponseModel?>
            ) {
                if (response.isSuccessful) {
                    _paymentMutable.value = response.body()

                }
            }

            override fun onFailure(call: retrofit2.Call<PaymentResponseModel?>, t: Throwable) {
                Log.d("ERROR", t.message.toString())
            }

        })
    }

    class PrescriptionNumberMissingException : Exception()
    class PrescriptionImageMissingException : Exception()
    class DiscountNumberMissingException : Exception()


}