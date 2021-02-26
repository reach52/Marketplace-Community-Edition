package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import reach52.marketplace.community.R
import reach52.marketplace.community.home.view.MainActivity
import reach52.marketplace.community.adapters.medication.OrderAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.medication_mapper.OrderMapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_received_orders.view.*

@ExperimentalUnsignedTypes
class ReceivedOrdersFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()

    private val orderAdapter = OrderAdapter {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, ReceivedOrdersDetailsFragment.newInstance(it))
                    .addToBackStack(null)
            commit()
        }
    }

    private val dispatchedOrdersQuery by lazy {
        DispergoDatabase.dispatchedOrdersView(context!!).createQuery()
    }

    private val dispatchedOrdersLiveQuery by lazy {
        dispatchedOrdersQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(dispatchedOrdersQuery::run, OrderMapper())
    }

    private val dispatchedOrders by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables.add(Search.query.subscribe({
            dispatchedOrdersLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

        dispatchedOrdersLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    dispatchedOrders.value?.dataSource?.invalidate()
                }
            }

            start()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dispatchedOrders.observe(viewLifecycleOwner, Observer { orderAdapter.submitList(it) })
        (activity as MainActivity).supportActionBar?.title = getString(R.string.dispatched_orders)
        return inflater.inflate(R.layout.fragment_received_orders, container, false).apply {
            receivedOrderRecyclerView.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                adapter = orderAdapter
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
