package reach52.marketplace.community.medicine.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentPurchasedMedicationListBinding
import reach52.marketplace.community.medicine.adapter.MedicineOrderAdapter
import reach52.marketplace.community.medicine.viewmodel.OrdersViewModel

class PurchasedMedicationListFragment : Fragment() {

	private lateinit var vm: OrdersViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
		val activity = activity as MedicationOrderStatusActivity
		vm = ViewModelProvider(activity)[OrdersViewModel::class.java]
		val b = FragmentPurchasedMedicationListBinding.inflate(inflater, container, false)

		vm.medicineOrders.observe(activity, { list ->
            /* b.orderRecyclerView.addItemDecoration(DividerItemDecoration(
					 context, LinearLayoutManager.VERTICAL))*/

            if (!list.isEmpty()) {
                b.orderRecyclerView.visibility = View.VISIBLE
                b.noMeds.visibility = View.GONE

            } else {
                b.orderRecyclerView.visibility = View.GONE
                b.noMeds.visibility = View.VISIBLE
            }

            b.orderRecyclerView.adapter = MedicineOrderAdapter(context!!, list) {


                vm.selectedOrder = it
                activity.goToMedicationProductDetailsFragment()
            }
        })
		vm.loadPurchaseOrders(context!!, vm.residentId)

//        val orderStatusArray = resources.getStringArray(R.array.order_status)
//        orderStatusSpinner.adapter = ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_dropdown_item,
//                orderStatusArray
//        )
		return b.root
	}

	override fun onResume() {
		super.onResume()
		(activity as MedicationOrderStatusActivity).supportActionBar?.title = getString(R.string.purchase_medication_list)
	}

}