package reach52.marketplace.community.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.couchbase.lite.QueryRow
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.InsuranceAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.InsuranceMapper
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insurance_list.view.*

@ExperimentalUnsignedTypes
class InsuranceFragment : Fragment() {
    private val disposables = CompositeDisposable()
    private val insuranceAdapter = InsuranceAdapter { id, insurance ->
        insuranceViewModel.selectedInsuranceID.value = id
        insuranceViewModel.selectedInsurance.value = insurance
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, InsurancePlanFragment())
            addToBackStack(null)
            commit()
        }
    }

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val insuranceQuery by lazy {
        DispergoDatabase.insuranceProductsView(context!!).createQuery()
    }

    private val insuranceLiveQuery by lazy {
        insuranceQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(insuranceQuery::run, InsuranceMapper())
    }

    private val insurance by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        insuranceLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    for (row : QueryRow in event.rows){
                        Log.d("aron", "event = " + row.document.properties.toString())
                    }
                    insurance.value?.dataSource?.invalidate()
                }
            }
            start()
        }
        disposables.add(Search.query.subscribe({
            insuranceLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, { throw NotImplementedError() }))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        insurance.observe(viewLifecycleOwner, Observer { insuranceAdapter.submitList(it) })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.insurance_label)
        return inflater.inflate(R.layout.fragment_insurance_list, container, false).apply {

            insurancesRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = insuranceAdapter
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = true
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}