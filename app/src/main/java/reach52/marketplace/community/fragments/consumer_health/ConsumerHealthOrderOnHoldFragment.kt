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
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.consumerHealth_mapper.ConsumerHealthOrderMapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health_order_on_hold.view.*

class ConsumerHealthOrderOnHoldFragment : androidx.fragment.app.Fragment() {

    private val disposables = CompositeDisposable()
    private val consumerHealthOrdersAdapter = ConsumerHealthOrderAdapter {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, ConsumerHealthOrderOnHoldDetailsFragment.newInstance(it) )
            addToBackStack(null)
            commit()
        }
    }

    private val consumerOnHoldOrderQuery by lazy{
        DispergoDatabase.consumerOnHoldOrdersView(context!!).createQuery()
    }

    private val consumerPendingOnHoldLiveQuery by lazy{
        consumerOnHoldOrderQuery.toLiveQuery()
    }

    private val consumerOnHoldOrder by lazy{
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    private val factory by lazy {
        EntityDataSourceFactory(consumerOnHoldOrderQuery::run, ConsumerHealthOrderMapper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        disposables.add(Search.query.subscribe({
            consumerPendingOnHoldLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

        consumerPendingOnHoldLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    consumerOnHoldOrder.value?.dataSource?.invalidate()
                }
            }
            start()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.on_hold_consumer)
        consumerOnHoldOrder.observe(viewLifecycleOwner, Observer { consumerHealthOrdersAdapter.submitList(it) })
        return inflater.inflate(R.layout.fragment_consumer_health_order_on_hold, container, false).apply{
            consumerOnHoldOrderRecyclerView.apply{
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                adapter = consumerHealthOrdersAdapter
            }
        }
    }
}
