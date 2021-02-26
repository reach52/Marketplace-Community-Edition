package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.consumer_health_adapter.ConsumerHealthCategoryAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.consumerHealth_mapper.ServiceOrProductMapper
import reach52.marketplace.community.viewmodels.ConsumerHealthViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health_category.view.*

class ConsumerHealthCategoryFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()

    private val consumerCategoryAdapter = ConsumerHealthCategoryAdapter {id , service->
        consumerHealthViewModel.selectedServiceOrProductID.value = id
        consumerHealthViewModel.selectedCategory.value = service
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, ConsumerHealthOrderDiscountFragment())
            addToBackStack(null)
            commit()
        }
    }

    private val consumerHealthViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ConsumerHealthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val serviceOrProductQuery by lazy {
        DispergoDatabase.serviceOrProductView(context!!).createQuery()
    }

    private val serviceOrProductQueryLiveQuery by lazy {
        serviceOrProductQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(serviceOrProductQuery::run, ServiceOrProductMapper())
    }

    private val category by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        serviceOrProductQueryLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    category.value?.dataSource?.invalidate()
                }
            }
            start()
        }

        disposables.add(Search.query.subscribe({
            serviceOrProductQueryLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, { throw NotImplementedError() }))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        category.observe(viewLifecycleOwner, Observer { consumerCategoryAdapter.submitList(it) })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.choose_your_category)
        return inflater.inflate(R.layout.fragment_consumer_health_category, container, false).apply {

            consumerHealthRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = consumerCategoryAdapter
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
