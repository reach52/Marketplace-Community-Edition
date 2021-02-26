package reach52.marketplace.community.insurance.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.activities.BaseActivity
import reach52.marketplace.community.databinding.ActivityPolicyOrderDetailsBinding
import reach52.marketplace.community.insurance.adapter.BeneficiaryListAdapter
import reach52.marketplace.community.insurance.adapter.PartyListAdapter
import reach52.marketplace.community.insurance.entity.Beneficiary
import reach52.marketplace.community.insurance.viewmodel.PolicyOrderViewModel

class PolicyOrderDetailsActivity : BaseActivity() {

	companion object {
		const val KEY_ID = "id"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val b = DataBindingUtil.setContentView<ActivityPolicyOrderDetailsBinding>(this, R.layout.activity_policy_order_details)

		val id = intent.getStringExtra(KEY_ID)

		val vm = ViewModelProvider(this)[PolicyOrderViewModel::class.java]
		vm.order.observe(this, Observer {

			b.order = it
			b.coveredMembersList.adapter = PartyListAdapter(this, it.parties)
			b.beneficaryList.adapter = BeneficiaryListAdapter(this, it.beneficiary as ArrayList<Beneficiary>, true)

		})

		vm.loadPolicyOrder(this, id)




	}
}