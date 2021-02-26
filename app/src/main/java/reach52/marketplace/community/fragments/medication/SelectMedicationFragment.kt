package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Some
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.medication.MedicationAdapter
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.medicine.mapper.MedicineMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.viewmodels.OrderViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.select_medication_fragment.view.*

@ExperimentalUnsignedTypes
class SelectMedicationFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()
    private val emitter: PublishSubject<Medicine> = PublishSubject.create()
    private val medicationsAdapter = MedicationAdapter {
        emitter.onNext(it)
        orderViewModel.selectedMedication.value = it
        orderViewModel.selectMedication = Some(it)
    }

    val selectedMedication: Observable<Medicine> = emitter

    private val medicationsQuery by lazy {
        DispergoDatabase.medicationsView(context!!).createQuery()
    }

    private val medicationsLiveQuery by lazy {
        medicationsQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(medicationsQuery::run, MedicineMapper())
    }

    private val medications by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    private val orderViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[OrderViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

//        medicationsQuery.apply{
//            startKey = orderViewModel.selectedSupplier.value?.identifier
//            endKey = orderViewModel.selectedSupplier.value?.identifier
//        }

        medicationsLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    medications.value?.dataSource?.invalidate()
                }
            }
            start()
        }

//        disposables.add(Search.query.subscribe({
//            medicationsLiveQuery.apply {
//                startKey = if (it.isNotEmpty()) it else orderViewModel.selectedSupplier.value?.identifier
//                endKey = if (it.isNotEmpty()) it + "\u9999" else orderViewModel.selectedSupplier.value?.identifier
//                queryOptionsChanged()
//            }
//        }, {
//            throw NotImplementedError()
//        }))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        medications.observe(viewLifecycleOwner, Observer { medicationsAdapter.submitList(it) })

        return inflater.inflate(R.layout.select_medication_fragment, container, false).apply {
//            (activity as MainActivity).supportActionBar?.title = getString(R.string.medicine_list)

            medicationsRecyclerView.apply {
                val linearLayoutManager = LinearLayoutManager(context!!)
                val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
                layoutManager = linearLayoutManager
                adapter = medicationsAdapter
                addItemDecoration(dividerItemDecoration)
            }
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search)?.isVisible = true
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
