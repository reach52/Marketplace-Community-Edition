package reach52.marketplace.community.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import arrow.core.Option
import arrow.core.getOrElse
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.InsuredBeneficiaryAdapter
import reach52.marketplace.community.models.CoverageInsured
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_beneficiary.view.*

class InsuranceBeneficiaryFragment : Fragment() {
    private val disposable = CompositeDisposable()
    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    @ExperimentalUnsignedTypes
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.beneficiary)
        return inflater.inflate(R.layout.fragment_insurance_beneficiary, container, false).apply {

            recyclerBeneficiary.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            insuranceViewModel.coveredFamilyMembers.observe(viewLifecycleOwner, Observer { covered ->
                recyclerBeneficiary.adapter = InsuredBeneficiaryAdapter(covered)
            })

            disposable.add(submit.clicks().subscribe({
                if (getBeneficiaries().isNotEmpty()) {
                    parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, AcknowledgementTakePhotoFragment())
                            .addToBackStack(null)
                            .commit()
                } else {
                    Toast.makeText(context, context.getString(R.string.please_add_family_member), Toast.LENGTH_SHORT).show()
                }
            }, {
                throw NotImplementedError()
            }))

            disposable.add(addFamilyMemberButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, AddBeneficiaryFragment())
                        .addToBackStack(null)
                        .commit()
            }, {
                throw NotImplementedError()
            }))
        }
    }

    private fun getBeneficiaries(): MutableList<CoverageInsured> {
        return Option.fromNullable(insuranceViewModel.coveredFamilyMembers.value).map { it }.getOrElse { mutableListOf() }
    }

}
