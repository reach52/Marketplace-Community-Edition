package reach52.marketplace.community.fragments.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.models.insurance.InsurancePurchase
import reach52.marketplace.community.models.policy_owner.PolicyOwner
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_declined_plan_details.view.*

@Suppress("UNCHECKED_CAST")
class DeclinedPlanDetailsFragment : Fragment() {
    lateinit var policyOwner : PolicyOwner
    private val disposables = CompositeDisposable()
    private val insurancePurchase by lazy {
        InsurancePurchase(DispergoDatabase.getInstance(context!!))
    }
    private val insuranceViewModel by lazy {
        Option.fromNullable(activity).map {
            ViewModelProvider(it)[InsuranceViewModel::class.java]
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insurance_declined_plan_details, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.plan_details)
            insuranceViewModel.map { model ->
                policyOwner = insurancePurchase.get(model.policyOwnerDocumentId.value?.toString()!!)
                planName.text = policyOwner.insurer.insurerName
                tier.text = policyOwner.insurer.plan[0].tier
                reviewedByDisplay.text = policyOwner.insurer.qualification.reviewedBy
                statusDisplay.text = policyOwner.status
                denialReasonDisplay.text = policyOwner.insurer.qualification.denialReason
            }


            disposables.add(goBackButton.clicks().subscribe{
                parentFragmentManager.popBackStack()
            })

        }
    }
}