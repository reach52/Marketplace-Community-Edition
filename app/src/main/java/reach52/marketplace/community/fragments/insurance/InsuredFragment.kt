package reach52.marketplace.community.fragments.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.insurance.InsuredAdapter
import reach52.marketplace.community.fragments.InsuranceConsentFragment
import reach52.marketplace.community.fragments.OtherDetailsFragment
import reach52.marketplace.community.fragments.PurchasePreviewFragment
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.InsuredMapper
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.OrderViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_insured_list.view.*


@ExperimentalUnsignedTypes
class InsuredFragment : Fragment() {

    private val disposable = CompositeDisposable()
    private lateinit var mModel: Option<OrderViewModel>

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val insuredAdapter = InsuredAdapter {

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, PurchasePreviewFragment.newInstance(it))
            addToBackStack(null)
            commit()
        }
    }


    private val insuredQuery by lazy {
        DispergoDatabase.insuredPlanView(context!!).createQuery()
    }

    private val insuredLiveQuery by lazy {
        insuredQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(insuredQuery::run, InsuredMapper())
    }

    private val insured by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }

        if (residentLogisticViewModel.isLogisticResident.value == true) {
            insuredQuery.apply {
                startKey = insuranceViewModel.selectedLogisticResident.value?.residentId
                endKey = insuranceViewModel.selectedLogisticResident.value?.residentId
            }
        } else {
            insuredQuery.apply {
                startKey = insuranceViewModel.selectedResident.value?.id
                endKey = insuranceViewModel.selectedResident.value?.id
            }
        }

        insuredLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    insured.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        insured.observe(viewLifecycleOwner, Observer { it -> insuredAdapter.submitList(it?.sortedByDescending { it?.record?.documentMeta?.dateCreated }) })

        return inflater.inflate(R.layout.fragment_insured_list, container, false).apply {
            insuredRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = insuredAdapter
            }

            when (residentLogisticViewModel.isLogisticResident.value) {
                true -> {
                    AgeValidationTextView.visibility = View.INVISIBLE
                    mModel.flatMap { it.selectedLogisticResident }.map {
                        when {
                            (it.age < 18 || it.age > 65) -> {
                                floatingActionButton.hide()
                                AgeValidationTextView.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                else -> {
                    AgeValidationTextView.visibility = View.INVISIBLE
                    mModel.flatMap { it.selectedResident }.map {
                        when {
                            (it.age < 18 || it.age > 65) -> {
                                floatingActionButton.hide()
                                AgeValidationTextView.visibility = View.VISIBLE

                            }
                        }
                    }
                }
            }

            disposable.add(floatingActionButton.clicks().subscribe({

                if (residentLogisticViewModel.isLogisticResident.value == true) {
                    parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, InsuranceConsentFragment())
                            .addToBackStack(null)
                            .commit()
                } else {
                    parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, OtherDetailsFragment())
                            .addToBackStack(null)
                            .commit()
                }

            }, {
                throw NotImplementedError()
            }))

        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

}
