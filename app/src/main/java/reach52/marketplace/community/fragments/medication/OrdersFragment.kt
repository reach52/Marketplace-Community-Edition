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
import reach52.marketplace.community.models.medication.OrderStatus
import reach52.marketplace.community.models.medication.Orders
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.medication_mapper.OrderMapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_orders.view.*

@ExperimentalUnsignedTypes
class OrdersFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()
    private val orderAdapter = OrderAdapter {
        parentFragmentManager.beginTransaction().apply {
            val order = orderDB.get(it)
            replace(R.id.fragment_container,
                    when (order.currentStatus.status) {
                        OrderStatus.Status.PENDING -> PendingOrderDetailsFragment.newInstance(it)
                        OrderStatus.Status.ON_HOLD -> OnHoldOrdersDetailsFragment.newInstance(it)
                        OrderStatus.Status.REJECTED -> RejectedOrdersDetailsFragment.newInstance(it)
                        OrderStatus.Status.ACCEPTED -> AcceptedOrderDetailsFragment.newInstance(it)
                        OrderStatus.Status.DISPATCHED -> ReceivedOrdersDetailsFragment.newInstance(it)
                        OrderStatus.Status.RECEIVED -> PickedUpOrderDetailsFragment.newInstance(it)
                        OrderStatus.Status.PICKED_UP -> DispatchedOrdersDetailsFragment.newInstance(it)
                        OrderStatus.Status.NOT_DELIVERED -> NotDeliveredOrdersDetailsFragment.newInstance(it)
                        else -> DeliveredOrdersDetailsFragment.newInstance(it)
                    })
            addToBackStack(null)
            commit()
        }
    }

    private val ordersQuery by lazy {
        DispergoDatabase.ordersView(context!!).createQuery()
    }

    private val ordersLiveQuery by lazy {
        ordersQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(ordersQuery::run, OrderMapper())
    }

    private val orders by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    private val orderDB by lazy {
        Orders(DispergoDatabase.getInstance(context!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        ordersLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    orders.value?.dataSource?.invalidate()
                }
            }

            start()
        }

        disposables.add(Search.query.subscribe({
            ordersLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        orders.observe(viewLifecycleOwner, Observer { orderAdapter.submitList(it) })
        (activity as MainActivity).supportActionBar?.title = getString(R.string.orders)
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