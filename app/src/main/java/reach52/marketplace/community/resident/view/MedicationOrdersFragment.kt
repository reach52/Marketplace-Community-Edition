package reach52.marketplace.community.resident.view


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.databinding.FragmentMedicationListBinding

import reach52.marketplace.community.medicine.view.MedicationOrderStatusActivity
import reach52.marketplace.community.medicine.view.MedicationPurchaseActivity
import reach52.marketplace.community.resident.viewmodel.ResidentViewModel

class MedicationOrdersFragment: Fragment() {

    private lateinit var vm: ResidentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val activity = activity as ResidentDetailsActivity
        val b = FragmentMedicationListBinding.inflate(inflater, container, false)

        vm = ViewModelProvider(activity)[ResidentViewModel::class.java]
//        vm.policyOrders.observe(activity, {
//
//            if (it.size == 0) {
//                b.emptyOrdersMessage.visibility = View.VISIBLE
//            } else {
//                b.emptyOrdersMessage.visibility = View.GONE
//                b.policyOrders.adapter = PolicyOrdersAdapter(context!!, it) {
//                    Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//        })

        b.orderStatusButton.setOnClickListener {
            startActivity(Intent(activity, MedicationOrderStatusActivity::class.java).apply {
                this.putExtra(MedicationOrderStatusActivity.KEY_RESIDENT_ID, vm.resident.id)
            })
        }

        b.newMedicationOrderButton.setOnClickListener {
            startActivity(Intent(activity, MedicationPurchaseActivity::class.java).apply {
                this.putExtra(MedicationPurchaseActivity.KEY_RESIDENT_ID, vm.resident.id)
            })
        }

        return b.root
    }

}