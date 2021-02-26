package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LivePagedListBuilder
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.consumerHealth_mapper.ConsumerHealthOrderMapper
import io.reactivex.disposables.CompositeDisposable

class ConsumerHealthOrderAcceptedFragment : androidx.fragment.app.Fragment()  {

    private val disposables = CompositeDisposable()
//    private val consumerHealthOrdersAdapter = ConsumerHealthOrderAdapter {
//        parentFragmentManager.beginTransaction().apply {
//            replace(R.id.fragment_container, ConsumerHealthOrderAcceptedDetailsFragment.newInstance(it) )
//            addToBackStack(null)
//            commit()
//        }
//    }

    private val consumerAcceptedOrderQuery by lazy{
        DispergoDatabase.consumerAcceptedOrdersView(context!!).createQuery()
    }

    private val consumerAcceptedOrderLiveQuery by lazy{
        consumerAcceptedOrderQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(consumerAcceptedOrderQuery::run, ConsumerHealthOrderMapper())
    }

    private val consumerAcceptedOrder by lazy{
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        disposables.add(Search.query.subscribe({
            consumerAcceptedOrderLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

        consumerAcceptedOrderLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    consumerAcceptedOrder.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.accepted_order)
//        consumerAcceptedOrder.observe(viewLifecycleOwner, Observer { consumerHealthOrdersAdapter.submitList(it) })
        return inflater.inflate(R.layout.fragment_consumer_health_order_accepted, container, false).apply{
//            consumerAcceptedOrderRecyclerView.apply{
//                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
//                adapter = consumerHealthOrdersAdapter
//            }
        }
    }
}
