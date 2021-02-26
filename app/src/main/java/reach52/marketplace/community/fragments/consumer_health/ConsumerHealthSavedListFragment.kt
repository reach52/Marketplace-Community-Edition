package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import arrow.core.Some
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.consumer_health_adapter.ConsumerHealthSavedAdapter
import reach52.marketplace.community.models.consumer_health_order.ShoppingCart
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.persistence.EntityDataSource
import reach52.marketplace.community.persistence.EntityDataSourceFactory
import reach52.marketplace.community.persistence.consumerHealth_mapper.ShoppingCartMapper
import reach52.marketplace.community.viewmodels.ConsumerHealthViewModel
import reach52.marketplace.community.viewmodels.InsuranceViewModel
import reach52.marketplace.community.viewmodels.OrderViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_consumer_health_saved_list.view.*

class ConsumerHealthSavedListFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private val emitter: PublishSubject<ShoppingCart> = PublishSubject.create()
    private lateinit var mModel: Option<OrderViewModel>

    private lateinit var chModel: Option<ConsumerHealthViewModel>
    private val consumerView by lazy {
        activity?.run {
            ViewModelProvider(this)[ConsumerHealthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    @ExperimentalUnsignedTypes
    private val savedCartAdapter = ConsumerHealthSavedAdapter{
        emitter.onNext(it)
        chModel.map { model -> model.selectedShoppingCart = Some(it) }
        consumerView.selectedProduct.value = it
    }
    val selectedServiceOrProduct: Observable<ShoppingCart> = emitter

    private lateinit var model: Option<OrderViewModel>

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val insuranceViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[InsuranceViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val savedProductQuery by lazy {
        DispergoDatabase.consumerSavedCart(context!!).createQuery()
    }

    private val savedProductLiveQuery by lazy {
        savedProductQuery.toLiveQuery()
    }

    private val factory by lazy {
        EntityDataSourceFactory(savedProductQuery::run, ShoppingCartMapper())
    }

    private val savedCart by lazy {
        LivePagedListBuilder(factory, EntityDataSource.PAGED_LIST_CONFIG).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        mModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }

        model = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }

        chModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[ConsumerHealthViewModel::class.java]
        }
        if (residentLogisticViewModel.isLogisticResident.value == true) {
            savedProductQuery.apply {
                startKey = insuranceViewModel.selectedLogisticResident.value?.residentId
                endKey = insuranceViewModel.selectedLogisticResident.value?.residentId
            }

        } else {
            savedProductQuery.apply {
                startKey = insuranceViewModel.selectedResident.value?.id
                endKey = insuranceViewModel.selectedResident.value?.id

            }
        }
        savedProductLiveQuery.apply {
            addChangeListener { event ->
                if (event.source == this) {
                    factory.fnEnumerator = { event.rows }
                    savedCart.value?.dataSource?.invalidate()
                }
            }
            start()
        }
    }

    @ExperimentalUnsignedTypes
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        savedCart.observe(viewLifecycleOwner, Observer { savedCartAdapter.submitList(it) })
        return inflater.inflate(R.layout.fragment_consumer_health_saved_list, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = context.getString(R.string.my_cart)

            savedProductsRecyclerView.apply {
                val linearLayoutManager = LinearLayoutManager(context!!)
                val dividerItemDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)
                layoutManager = linearLayoutManager
                adapter = savedCartAdapter
                addItemDecoration(dividerItemDecoration)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

}
