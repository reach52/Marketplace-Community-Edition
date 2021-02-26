package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.consumer_health_adapter.ConsumerHealthSelectProductsAdapter
import reach52.marketplace.community.models.Search
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.consumerHealth_mapper.ServiceOrProductMapper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_consumer_health_select_products.view.*


class ConsumerHealthSelectProductsFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()
    private val emitter: PublishSubject<ServiceOrProduct> = PublishSubject.create()
    private val consumerHealthProductsAdapter = ConsumerHealthSelectProductsAdapter {
        emitter.onNext(it)
    }
    val selectedServiceOrProduct: Observable<ServiceOrProduct> = emitter

    private val productsQuery by lazy {
        DispergoDatabase.productView(context!!).createQuery()
    }

    private val productsQueryLiveQuery by lazy {
            productsQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(productsQuery::run, ServiceOrProductMapper())
    }

    private val products by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        disposables.add(Search.query.subscribe({
            productsQueryLiveQuery.apply {
                startKey = if (it.isNotEmpty()) it else null
                endKey = if (it.isNotEmpty()) it + "\u9999" else null
                queryOptionsChanged()
            }
        }, {
            throw NotImplementedError()
        }))

        productsQueryLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    products.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        products.observe(viewLifecycleOwner, Observer {consumerHealthProductsAdapter.submitList(it) })
        return inflater.inflate(R.layout.fragment_consumer_health_select_products, container, false).apply{
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.your_order)

            productsRecyclerView.apply {
                val linearLayoutManager = LinearLayoutManager(context!!)
                val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
                layoutManager = linearLayoutManager
                adapter = consumerHealthProductsAdapter
                addItemDecoration(dividerItemDecoration)
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
