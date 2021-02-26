package reach52.marketplace.community.resident.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.databinding.FragmentPolicyOrdersBinding

import reach52.marketplace.community.insurance.view.InsurancePurchaseActivity
import reach52.marketplace.community.insurance.view.PolicyOrderDetailsActivity
import reach52.marketplace.community.resident.adapter.PolicyOrdersAdapter
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel

class PolicyOrdersFragment : Fragment() {

	private lateinit var vm: ResidentViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {

		val activity = activity as ResidentDetailsActivity
		val b = FragmentPolicyOrdersBinding.inflate(inflater, container, false)

		vm = ViewModelProvider(activity)[ResidentViewModel::class.java]
		vm.policyOrders.observe(activity, {

			if (it.size == 0) {
				b.emptyOrdersLayout.visibility = View.VISIBLE
				b.policyOrders.visibility = View.GONE
			} else {
				b.policyOrders.visibility = View.VISIBLE
				b.emptyOrdersLayout.visibility = View.GONE
				b.policyOrders.adapter = PolicyOrdersAdapter(activity, it) {

					activity.startActivity(Intent(activity, PolicyOrderDetailsActivity::class.java).apply {
						putExtra(PolicyOrderDetailsActivity.KEY_ID, it.id)
					})

				}
			}

		})

		b.floatingActionButton.setOnClickListener {
			startActivity(Intent(activity, InsurancePurchaseActivity::class.java).apply {
				this.putExtra(InsurancePurchaseActivity.KEY_RESIDENT_ID, vm.resident.id)
			})
		}

		// when click on purchase insurance Text
		b.emptyOrdersMessage.setOnClickListener {
			startActivity(Intent(activity, InsurancePurchaseActivity::class.java).apply {
				this.putExtra(InsurancePurchaseActivity.KEY_RESIDENT_ID, vm.resident.id)
			})
		}

		return b.root
	}

	override fun onStart() {
		super.onStart()
		vm.loadPolicyOrdersForResident(context!!, vm.residentId)
	}

}