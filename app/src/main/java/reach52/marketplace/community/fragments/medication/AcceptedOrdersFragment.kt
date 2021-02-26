package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.medication.OrderAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.medication_mapper.OrderMapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_orders.view.*

@ExperimentalUnsignedTypes
class AcceptedOrdersFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()


    private val orderAdapter = OrderAdapter {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, AcceptedOrderDetailsFragment.newInstance(it))
            addToBackStack(null)
            commit()
        }
    }

    private val acceptedOrdersQuery by lazy {
        DispergoDatabase.acceptedOrdersView(context!!).createQuery()
    }

    private val acceptedOrdersLiveQuery by lazy {
        acceptedOrdersQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(acceptedOrdersQuery::run, OrderMapper())
    }

    private val acceptedOrders by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables.add(Search.query.subscribe({
            acceptedOrdersLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

        acceptedOrdersLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    acceptedOrders.value?.dataSource?.invalidate()
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
        acceptedOrders.observe(viewLifecycleOwner, Observer { orderAdapter.submitList(it) })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.accepted_order)
        return inflater.inflate(R.layout.fragment_orders, container, false).apply {
            ordersView.apply {
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
