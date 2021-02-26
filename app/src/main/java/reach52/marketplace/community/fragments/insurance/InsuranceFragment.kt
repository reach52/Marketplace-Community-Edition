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
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance_cambodia.InsuranceAdapter
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.insurer_mapper.InsurerDocumentMapper
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import kotlinx.android.synthetic.main.fragment_insurance.view.*

class InsuranceFragment : Fragment() {

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val insuranceAdapter = InsuranceAdapter { id, document ->
        insuranceViewModel.selectedInsurerId.value = id
        insuranceViewModel.selectedInsurer.value = document
        insuranceViewModel.unit.value = document.locale.pricing.unit

        val requirements = document.requirements
        for (requirement in requirements.indices){
            if(document.requirements[requirement].identifier == "cert_num"){
                insuranceViewModel.selectedInsurerRequirement.value = document.requirements[requirement]
            }
        }

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, InsurancePlansFragment())
            addToBackStack(null)
            commit()
        }
    }

    private val insurerQuery by lazy {
        DispergoDatabase.insurerView(context!!).createQuery()
    }

    private val insurerLiveQuery by lazy {
        insurerQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(insurerQuery::run, InsurerDocumentMapper())
    }

    private val insurer by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        insurerLiveQuery.apply {
            addChangeListener { event ->
                if(event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    insurer.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_insurance, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.insurance_label)

            insurer.observe(viewLifecycleOwner, Observer { insuranceAdapter.submitList(it) })
            insurersRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = insuranceAdapter
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

}