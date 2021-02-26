package reach52.marketplace.community.fragments.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
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
import reach52.marketplace.community.adapters.insurance_cambodia.InsurancePlanAdapter
import reach52.marketplace.community.models.insurance.Formulary
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import kotlinx.android.synthetic.main.fragment_insurance_plans.view.*
import kotlin.math.min

class InsurancePlansFragment : Fragment() {

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val insurancePlanAdapter by lazy {
        InsurancePlanAdapter {
            insuranceViewModel.selectedInsurerPlan.value = it

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, InsurancePlanDetailFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    private val insurancePlanLiveData by lazy {
        val formulary = Option.fromNullable(insuranceViewModel.selectedInsurer.value)
                .map { it.formulary }.getOrElse { ArrayList() }

        val factory = object : DataSource.Factory<Int, Formulary>() {
            override fun create(): DataSource<Int, Formulary> =
                object : PositionalDataSource<Formulary>() {
                    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Formulary>) {
                        val end = params.startPosition + params.loadSize - 1
                        callback.onResult((params.startPosition..end).map { formulary[it] })
                    }

                    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Formulary>) {
                        val count = formulary.size
                        if(count > 0) {
                            val start = min(params.requestedStartPosition, count - 1)
                            val end = min(params.requestedLoadSize, count - 1)
                            callback.onResult((start..end).map { formulary[it] }, start, count)
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
        insurancePlanLiveData.observe(viewLifecycleOwner, Observer { insurancePlanAdapter.submitList(it) })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.insurance_plan)

        return inflater.inflate(R.layout.fragment_insurance_plans, container, false).apply {
            insurancePlanRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = insurancePlanAdapter
            }
        }
    }

}
