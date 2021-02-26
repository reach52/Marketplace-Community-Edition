package reach52.marketplace.community.medicine.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.medicine.entity.MedicineOrder
import reach52.marketplace.community.medicine.repo.OrderRepo
import io.reactivex.Completable

class OrdersViewModel : ViewModel() {
	lateinit var residentId: String
	val medicineOrders = MutableLiveData<ArrayList<MedicineOrder>>()
	var medicineList = listOf<MedicineOrder.Item>()
	lateinit var selectedOrder: MedicineOrder

	@SuppressLint("CheckResult")
	fun loadPurchaseOrders(context: Context, id: String) {
		OrderRepo.getOrdersForResident(context, id).subscribe(
				{ orders ->
					medicineOrders.value = orders
				}, {}
		)
	}

	fun setOrderItems(list: List<MedicineOrder.Item>) {
		medicineList = list
	}

	fun getDiscount(): Double {

		val selectedDiscount = selectedOrder.discount

		var discountValue = 0.0

		if (selectedDiscount != null) {

			val value = selectedDiscount.value
			val isPer = selectedDiscount.isPercentage

			discountValue = if (isPer) {
				(value * selectedOrder.subTotal) / 100
			} else {
				value
			}

		}

		return String.format("%.2f", discountValue).toDouble()

	}

	fun updateOrderToReceived(context: Context) = updateOrderStatus(context, "Received")

	private fun updateOrderStatus(context: Context, status: String) = Completable.create {

		try {
			OrderRepo.updateOrderStatus(context, selectedOrder, status)
			it.onComplete()
		} catch (e: OrderRepo.UpdateFailedException) {
			it.onError(e)
		}


	}

}
