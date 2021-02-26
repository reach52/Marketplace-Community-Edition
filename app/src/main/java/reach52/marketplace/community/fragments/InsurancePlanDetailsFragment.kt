package reach52.marketplace.community.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.SpecificCostAdapter
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_plan_details.*
import kotlinx.android.synthetic.main.fragment_insurance_plan_details.view.*
import org.threeten.bp.format.DateTimeFormatter

@ExperimentalUnsignedTypes
class InsurancePlanDetailsFragment : Fragment() {
    private val disposable = CompositeDisposable()
    private val specificCostAdapter = SpecificCostAdapter()

    private val mModel by lazy {
        Option.fromNullable(activity).map {
            ViewModelProvider(it)[InsuranceViewModel::class.java]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mModel.map { model ->
            model.selectedInsurancePlanExpiryDate.observe(viewLifecycleOwner, Observer {
                expiryDateTextView.text = it.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            })

            model.selectedInsurancePlanPrice.observe(viewLifecycleOwner, Observer {
                priceTextView.text = it.toPhilippinesCurrency()
            })

            model.selectedInsurancePlanSpecificCosts.observe(viewLifecycleOwner, Observer {
                specificCostAdapter.submitList(it)
            })

            model.selectedInsurancePlanTitle.observe(viewLifecycleOwner, Observer {
                titleTextView.text = it
            })
        }

        return inflater.inflate(R.layout.fragment_insurance_plan_details, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.insurance_plan_details)

            specificCostRecycler.apply {
                val linearLayoutManager = LinearLayoutManager(activity)
                val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
                layoutManager = linearLayoutManager
                adapter = specificCostAdapter
                addItemDecoration(dividerItemDecoration)
            }

            disposable.add(viewInsurancePolicyButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, InsurancePolicyDetailsFragment())
                        .addToBackStack(null)
                        .commit()
            }, {
                throw NotImplementedError()
            }))

            disposable.add(proceedButton.clicks().subscribe({
                parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, InsuranceBeneficiaryFragment())
                        .addToBackStack(null)
                        .commit()
            }, {
                throw NotImplementedError()
            }))

            disposable.add(cancelButton.clicks().subscribe {
                parentFragmentManager.popBackStack()
            })
        }
    }
}