package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.consumer_health_adapter.ConsumerHealthOrderAdapter
import reach52.marketplace.community.fragments.medication.DeliveredOrdersDetailsFragment
import reach52.marketplace.community.fragments.medication.NotDeliveredOrdersDetailsFragment
import reach52.marketplace.community.fragments.medication.PickedUpOrderDetailsFragment
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrders
import reach52.marketplace.community.models.medication.OrderStatus
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.consumerHealth_mapper.ConsumerHealthOrderMapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health_orders.view.*

class ConsumerHealthOrdersFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()
    private val consumerHealthOrdersAdapter = ConsumerHealthOrderAdapter {
        parentFragmentManager.beginTransaction().apply {
            val consumerOrders = consumerHealthOrderDB.get(it)
            replace(R.id.fragment_container,
                    when (consumerOrders.currentStatus.status) {
                        OrderStatus.Status.PENDING -> ConsumerHealthOrderPendingDetailsFragment.newInstance(it)
                        OrderStatus.Status.ON_HOLD -> ConsumerHealthOrderOnHoldDetailsFragment.newInstance(it)
                        OrderStatus.Status.REJECTED -> ConsumerHealthOrderRejectedDetailsFragment.newInstance(it)
                        OrderStatus.Status.ACCEPTED -> ConsumerHealthOrderAcceptedDetailsFragment.newInstance(it)
                        OrderStatus.Status.DISPATCHED -> ConsumerHealthOrderReceivedDetailsFragment.newInstance(it)
                        OrderStatus.Status.RECEIVED -> PickedUpOrderDetailsFragment.newInstance(it)
                        OrderStatus.Status.PICKED_UP -> ConsumerHealthOrderDispatchedDetailsFragment.newInstance(it)
                        OrderStatus.Status.NOT_DELIVERED -> NotDeliveredOrdersDetailsFragment.newInstance(it)
                        else -> DeliveredOrdersDetailsFragment.newInstance(it)
                    })
            addToBackStack(null)
            commit()
        }
    }

    private val consumerHealthOrdersQuery by lazy{
        DispergoDatabase.consumerOrdersView(context!!).createQuery()
    }

    private val consumerHealthOrdersLiveQuery by lazy{
        consumerHealthOrdersQuery.toLiveQuery()
    }

    private val factory by lazy{
        EntityDataSourceFactory(consumerHealthOrdersQuery::run, ConsumerHealthOrderMapper())
    }

    private val consumerHealthOrders by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }


    private val consumerHealthOrderDB by lazy{
        ConsumerHealthOrders(DispergoDatabase.getInstance(context!!))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        consumerHealthOrdersLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    consumerHealthOrders.value?.dataSource?.invalidate()
                }
            }

            start()
        }

        disposables.add(Search.query.subscribe({
            consumerHealthOrdersLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        consumerHealthOrders.observe(viewLifecycleOwner, Observer { consumerHealthOrdersAdapter.submitList(it)  })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.orders)
        return inflater.inflate(R.layout.fragment_consumer_health_orders, container, false).apply {
            consumerHealthOrdersView.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                adapter = consumerHealthOrdersAdapter
            }
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
