package healthcare.alliedworld.dispergo.medicine.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentPaymentMenuBinding
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.medicine.view.MedicationPurchaseActivity
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel

class PaymentFragment : Fragment() {

    lateinit var activity: MedicationPurchaseActivity
    lateinit var binding: FragmentPaymentMenuBinding
    lateinit var viewModel: MedicationPurchaseViewModel
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = requireActivity() as MedicationPurchaseActivity
        viewModel = ViewModelProvider(activity)[MedicationPurchaseViewModel::class.java]
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment_menu, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isInternetAvailable = NetworkManager.isInternetAvailable()
        binding.xendilbtn.isEnabled = isInternetAvailable
//        binding.xendilbtn.setOnClickListener {
//            viewModel.updateSubOrderPaymentStatusOnline("Online")!!.subscribe({
//                Log.e("PaymentStatusAndMode", "Updated")
//                activity.goToPaymentUIFragment()
//            }, {
//                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT)
//                    .show()
//            })
//
//        }

//        binding.cashOnDeliverybtn.setOnClickListener {
//            viewModel.updateSubOrderPaymentStatusOnline("COD")!!.subscribe({
//                Log.e("PaymentStatusAndMode", "Updated")
//                activity.finish()
//            }, {
//                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT)
//                    .show()
//            })
//        }

        binding.xendilbtn.setOnClickListener {

            activity.goToPaymentUIFragment()
            Log.d("Xendid", "paymentFramgent : webpage called")

        }

        binding.cashOnDeliverybtn.setOnClickListener {
//            viewModel.saveOrder(context!!)
            Toast.makeText(context, "Your order is created", Toast.LENGTH_LONG).show()
            activity.finish()
        }

    }

}