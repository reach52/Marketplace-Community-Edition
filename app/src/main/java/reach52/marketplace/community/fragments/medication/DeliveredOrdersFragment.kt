package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.home.view.MainActivity
import reach52.marketplace.community.adapters.medication.OrderAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.medication_mapper.OrderMapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_orders.view.*

@ExperimentalUnsignedTypes
class DeliveredOrdersFragment : androidx.fragment.app.Fragment() {

    private val disposables = CompositeDisposable()

    private val orderAdapter = OrderAdapter {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, DeliveredOrdersDetailsFragment.newInstance(it))
            addToBackStack(null)
            commit()
        }
    }

    private val deliveredOrdersQuery by lazy {
        DispergoDatabase.deliveredOrdersView(context!!).createQuery()
    }

    private val deliveredOrderLiveQuery by lazy {
        deliveredOrdersQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(deliveredOrderLiveQuery::run, OrderMapper())
    }

    private val deliveredOrders by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        disposables.add(Search.query.subscribe({
            deliveredOrderLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

        deliveredOrderLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    deliveredOrders.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        deliveredOrders.observe(viewLifecycleOwner, Observer { orderAdapter.submitList(it) })
        (activity as MainActivity).supportActionBar?.title = getString(R.string.delivered_order)
        return inflater.inflate(R.layout.fragment_orders, container, false).apply {
            ordersView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = orderAdapter
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}