package reach52.marketplace.community.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import arrow.core.getOrElse
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.InsurancePlanAdapter
import reach52.marketplace.community.models.InsurancePlan
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import kotlinx.android.synthetic.main.fragment_insurance_list.view.*
import kotlin.math.min

@ExperimentalUnsignedTypes
class InsurancePlanFragment : Fragment() {

    private val planAdapter by lazy {
        InsurancePlanAdapter {
            val insuranceViewModel = activity?.run {
                ViewModelProvider(this)[InsuranceViewModel::class.java]
            } ?: throw Exception("Invalid Activity")
            insuranceViewModel.selectedInsuranceInsurancePlan.value = it

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, InsurancePlanDetailsFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val plan by lazy {
        val plan = Option.fromNullable(insuranceViewModel.selectedInsurance.value)
                .map { it.insurancePlan }.getOrElse { ArrayList() }

        val factory = object : DataSource.Factory<Int, InsurancePlan>() {
            override fun create(): DataSource<Int, InsurancePlan> =
                    object : PositionalDataSource<InsurancePlan>() {
                        override fun loadRange(
                                params: LoadRangeParams,
                                callback: LoadRangeCallback<InsurancePlan>
                        ) {
                            val end = params.startPosition + params.loadSize - 1
                            callback.onResult((params.startPosition..end).map { plan[it] })
                        }

                        override fun loadInitial(
                                params: LoadInitialParams,
                                callback: LoadInitialCallback<InsurancePlan>
                        ) {
                            val count = plan.size
                            if (count > 0) {
                                val start = min(params.requestedStartPosition, count - 1)
                                val end = min(params.requestedLoadSize, count - 1)
                                callback.onResult((start..end).map { plan[it] }, start, count)
                            } else {
                                callback.onResult(listOf(), 0, count)
                            }
                        }
                    }
        }
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        plan.observe(viewLifecycleOwner, Observer { planAdapter.submitList(it) })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.insurance_plan)
        return inflater.inflate(R.layout.fragment_insurance_list, container, false).apply {
            insurancesRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = planAdapter
            }
        }
    }
}