package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Option
import arrow.instances.option.monad.binding
import com.jakewharton.rxbinding3.view.clicks
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.consumer_health_adapter.ConsumerHealthOrderItemAdapter
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.consumer_health_order.Carts
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrders
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.viewmodels.ConsumerHealthViewModel
import reach52.marketplace.community.viewmodels.OrderViewModel
import reach52.marketplace.community.viewmodels.ResidentLogisticViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_consumer_health_basket.view.*

class ConsumerHealthBasketFragment : androidx.fragment.app.Fragment() {
    private val disposables = CompositeDisposable()
    private val viewDisposables = CompositeDisposable()
    private val selectServiceOrProductAdapter = ConsumerHealthOrderItemAdapter()
    private val selectServiceOrProductFragment = ConsumerHealthSelectProductsFragment()
    private lateinit var mModel: Option<OrderViewModel>

    private val residentLogisticViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ResidentLogisticViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val consumerViewModel by lazy {
        activity?.run {
            ViewModelProvider(this)[ConsumerHealthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    private val consumerHealthOrders by lazy{
        ConsumerHealthOrders(DispergoDatabase.getInstance(context!!))
    }

    private val carts by lazy{
        Carts(DispergoDatabase.getInstance(context!!))
    }


//    private val user by lazy {
//        Auth.currentSession(context!!).map {
//            it.user
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mModel = Option.fromNullable(activity).map {
            ViewModelProvider(it)[OrderViewModel::class.java]
        }

        disposables.add(selectServiceOrProductFragment.selectedServiceOrProduct.subscribe({
            val itemsProducts = it.copy()
            selectServiceOrProductAdapter.addProducts(itemsProducts)
            parentFragmentManager.popBackStack()
        }, {
            Log.w("ConsumerBasketFragment", "selectedProducts", it)
            throw NotImplementedError()
        }))
    }

    @ExperimentalUnsignedTypes
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_consumer_health_basket, container, false).apply {
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.your_basket)
            selectedConsumerHealthRecyclerView.apply {
                layoutManager = LinearLayoutManager(context!!)
                adapter = selectServiceOrProductAdapter
            }

            mModel.fold({}, {
                it.discountIdNumber.fold({
                    discountIdNumberTextView.visibility = View.INVISIBLE
                }, { id ->
                    discountIdNumberTextView.text = id
                })
            })

            viewDisposables.add(selectServiceOrProductAdapter.orderTotal.subscribe({
                checkoutButtonConsumerHealth.isEnabled = selectServiceOrProductAdapter.itemCount > 0
                if (selectServiceOrProductAdapter.itemCount > 0) {
                    val orderItems = selectServiceOrProductAdapter.orderProducts()
                    subTotalTextView.text = orderItems.list.map { it.basePriceTotal }.sum().toPhilippinesCurrency()
                    mModel.flatMap { model -> model.selectedDiscount }.fold({
                        vatExemptTextView.text = 0.0.toPhilippinesCurrency()
                        discountTextView.text = 0.0.toPhilippinesCurrency()
                        totalTextView.text = orderItems.list.map { it.basePriceTotal }.sum().toPhilippinesCurrency()
                    }, {
                        val vatExempted = orderItems.list.map { it.vatValue }.sum()
                        val discountValue = orderItems.list.map { it.discountValue() }.sum()
                        vatExemptTextView.text = vatExempted.toPhilippinesCurrency()
                        discountTextView.text = discountValue.toPhilippinesCurrency()
                        totalTextView.text = orderItems.list.map { it.total() }.sum().toPhilippinesCurrency()
                    })
                }
                saveProductsButton.isEnabled = selectServiceOrProductAdapter.itemCount > 0
                if (selectServiceOrProductAdapter.itemCount > 0) {
                    val orderItems = selectServiceOrProductAdapter.orderProducts()
                    subTotalTextView.text = orderItems.list.map { it.basePriceTotal }.sum().toPhilippinesCurrency()
                    mModel.flatMap { model -> model.selectedDiscount }.fold({
                        vatExemptTextView.text = 0.0.toPhilippinesCurrency()
                        discountTextView.text = 0.0.toPhilippinesCurrency()
                        totalTextView.text = orderItems.list.map { it.basePriceTotal }.sum().toPhilippinesCurrency()
                    }, {
                        val vatExempted = orderItems.list.map { it.vatValue }.sum()
                        val discountValue = orderItems.list.map { it.discountValue() }.sum()
                        vatExemptTextView.text = vatExempted.toPhilippinesCurrency()
                        discountTextView.text = discountValue.toPhilippinesCurrency()
                        totalTextView.text = orderItems.list.map { it.total() }.sum().toPhilippinesCurrency()
                    })
                }
            }, {
                Log.w("ConsumerBasketFragment", "orderProductTotal", it)
                throw NotImplementedError()
            }))

            if (residentLogisticViewModel.isLogisticResident.value == true) {
                mModel.flatMap { it.selectedLogisticResident }.map {
                    consumerNameTextView.text = it.name
                    consumerAddressOTC.text = it.address
                    consumerAgeTextView.text = it.age.toString()
                    consumerGenderTextView.text = it.gender
                }
            } else {
                mModel.flatMap { it.selectedResident }.map {
                    consumerNameTextView.text = it.name
                    consumerAddressOTC.text = it.address.text
                    consumerGenderTextView.text = when (it.gender) {
                        "M" -> "Male"
                        "F" -> "Female"
                        else -> "Unknown: ${it.gender}"
                    }
                    consumerAgeTextView.text = it.age.toString()
                }
            }
            disposables.add(saveButtonProduct.clicks().subscribe({
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectServiceOrProductFragment)
                        .addToBackStack(null)
                        .commit()
            },{
                    Log.w("ConsumerHealthFragment", "checkoutButtonConsumerHealth", it)
                    throw NotImplementedError()
            }))

            saveProductsButton.setOnClickListener {
                saveProductsButton.isEnabled = false
                saveProductsButton.text = getString(R.string.saved)
                consumerViewModel.selectedProduct.value
                binding{
                    val model = mModel.bind()
                    val items = selectServiceOrProductAdapter.orderProducts()

                    if(residentLogisticViewModel.isLogisticResident.value == true){
                        val resident = model.selectedLogisticResident.bind()
                        carts.residentLogistic(
                                resident = resident,
                                orderLine = items
                        )
                        Log.d("cart", carts.toString())
                    }else{
                        val resident = model.selectedResident.bind()
                        carts.residentAccess(
                                resident = resident,
                                orderLine = items
                        )
                    }
                    model.reset()
                }
                Toast.makeText(context, "Orders has been saved", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }

            checkoutButtonConsumerHealth.setOnClickListener{
                checkoutButtonConsumerHealth.isEnabled = false
                checkoutButtonConsumerHealth.text = getText(R.string.order_processing)

                val consumerCtx = context
//               binding {
//                   val model = mModel.bind()
//                   val  items = selectServiceOrProductAdapter.orderProducts()
//                   val orderId: String
//                   if (residentLogisticViewModel.isLogisticResident.value == true) {
//                       val resident = model.selectedLogisticResident.bind()
//                       orderId = consumerHealthOrders.residentLogistic(
//                               discount = model.selectedDiscount,
//                               discountIDNumber = model.discountIdNumber,
//                               items = items,
//                               resident = resident,
//                               hcpRequestNumber = model.hcpNumber,
//                               user = user
//                       )
//                   } else {
//                       val resident = model.selectedResident.bind()
//                       orderId = consumerHealthOrders.residentAccess(
//                               discount = model.selectedDiscount,
//                               discountIDNumber = model.discountIdNumber,
//                               items = items,
//                               resident = resident,
//                               hcpRequestNumber = model.hcpNumber,
//                               user = user
//                       )
//                   }
//
//                   val base58 = UUID.fromString(orderId).base58String()
//
//                   AlertDialog.Builder(consumerCtx).setTitle("Order Created").setMessage("Effectively Order Number: $base58").setPositiveButton("Okay") { _, _ ->
//                       model.reset()
//
//                       val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
//                       val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
//                       val toggle = ActionBarDrawerToggle(
//                               activity, drawer, toolbar, R.string.navigation_drawer_open,
//                               R.string.navigation_drawer_close)
//                       toggle.syncState()
//
//                       parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                   }.create().apply {
//                       setOnCancelListener {
//                           model.reset()
//
//                           val toolbar: Toolbar = activity!!.findViewById(R.id.toolbar)
//                           val drawer: DrawerLayout = activity!!.findViewById(R.id.drawer_layout)
//                           val toggle = ActionBarDrawerToggle(
//                                   activity, drawer, toolbar, R.string.navigation_drawer_open,
//                                   R.string.navigation_drawer_close)
//                           toggle.syncState()
//
//                           parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                       }
//                   }.show()
//               }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }

    override fun onDestroyView() {
        viewDisposables.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
