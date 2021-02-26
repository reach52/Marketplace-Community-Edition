package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentMedicinePurchaseDetailsBinding
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.extensions.utils.getCurrencyString
import reach52.marketplace.community.medicine.adapter.MedicineOrderItemAdapter
import reach52.marketplace.community.medicine.viewmodel.OrdersViewModel
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class PurchaseDetailsFragment : Fragment() {

	private lateinit var vm: OrdersViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val activity = activity as MedicationOrderStatusActivity
		vm = ViewModelProvider(activity)[OrdersViewModel::class.java]
		val order = vm.selectedOrder
		val trackingCode = UUID.fromString(order.id).base58String()
		val b = FragmentMedicinePurchaseDetailsBinding.inflate(inflater, container, false)
		Log.v("taaag", "order: ${order.id}, tracking: $trackingCode")

		b.apply {

			trackingCodeTextView.text = trackingCode
			orderStatusTextView.text = order.currentStatus.toUpperCase(Locale.ROOT)
			orderDateTextView.text = order.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
			physicianNameTextView.text = order.physicianName
			prescriptionNumberEditText.text = order.prescriptionNumber
			discountIdNumberTextView.text = order.discountIdNumber
			subTotalTextView.text = getCurrencyString(order.subTotal.toString(), order.isoCurrency)
			taxTextView.text = getCurrencyString(order.payableTax.toString(), order.isoCurrency)
			discountTextView.text = getCurrencyString("- ${vm.getDiscount()}", order.isoCurrency)
			feeTextView.text = getCurrencyString(order.deliveryFee.toString(), order.isoCurrency)
			totalTextView.text = getCurrencyString(order.total.toString(), order.isoCurrency)

			vm.setOrderItems(order.items)

			vm.medicineList.apply {
				orderItemsRecyclerView.addItemDecoration(DividerItemDecoration(
						context, LinearLayoutManager.VERTICAL))
				orderItemsRecyclerView.adapter = MedicineOrderItemAdapter(context!!, order.isoCurrency,  this)
			}

			if (order.currentStatus.toLowerCase() != "dispatched") {
				orderReceivedBtn.visibility = View.GONE
			}

			orderReceivedBtn.isEnabled = order.currentStatus.toLowerCase() != "received"
			orderReceivedBtn.setOnClickListener {

				vm.updateOrderToReceived(activity).subscribe(
						{
							orderStatusTextView.text = order.currentStatus.toUpperCase(Locale.ROOT)
							orderReceivedBtn.isEnabled = false
							vm.loadPurchaseOrders(context!!, vm.residentId)
						},
						{
							Toast.makeText(activity, "Update failed", Toast.LENGTH_SHORT).show()
						}
				)

			}

		}

		return b.root
	}

	override fun onResume() {
		super.onResume()
		(activity as MedicationOrderStatusActivity).supportActionBar?.title = getString(R.string.purchase_medication_details)
	}
}