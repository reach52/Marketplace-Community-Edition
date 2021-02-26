package reach52.marketplace.community.fragments.consumer_health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import arrow.core.getOrElse
import reach52.marketplace.community.R
import reach52.marketplace.community.adapters.consumer_health_adapter.ConsumerHealthOrderLinesAdapter
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrder
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrders
import reach52.marketplace.community.persistence.DispergoDatabase
import kotlinx.android.synthetic.main.picked_up_order_details.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class ConsumerHealthOrderPickedUpDetailsFragment :androidx.fragment.app.Fragment() {

    companion object{
        private const val KEY_CONSUMER_ORDER_ID = "consumerOrderId"
        fun newInstance(consumerOrderId: String) : ConsumerHealthOrderPickedUpDetailsFragment{
            return ConsumerHealthOrderPickedUpDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CONSUMER_ORDER_ID, consumerOrderId)
                }
            }
        }
    }

    private val consumerOrderId by lazy{
        arguments?.getString(KEY_CONSUMER_ORDER_ID)!!
    }

    @ExperimentalUnsignedTypes
    private val orderIdBase58 by lazy {
        UUID.fromString(consumerOrderId).base58String()
    }

    private val consumerHealthOrderDB by lazy{
        ConsumerHealthOrders(DispergoDatabase.getInstance(context!!))
    }

//    private val user by lazy {
//        Auth.currentSession(context!!).map {
//            it.user
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.received_order_details)
        return inflater.inflate(R.layout.fragment_consumer_health_order_picked_up_details, container, false)
    }

    @ExperimentalUnsignedTypes
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        orderItemsView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        displayOrder(consumerHealthOrderDB.get(consumerOrderId))
    }

    @ExperimentalUnsignedTypes
    private fun displayOrder(order: ConsumerHealthOrder) {
        trackingCodeTextView.text = orderIdBase58
        patientNameTextView.text = order.customer.firstName.plus("").plus(order.customer.lastName)
        orderStatusTextView.text = order.currentStatus.status.toString()
        orderDateTextView.text = order.currentStatus.statusDate.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        orderItemsView.adapter = ConsumerHealthOrderLinesAdapter(order.orderLines)

        discountIdNumberTextView.text = order.discountIDNumber.getOrElse { "none" }
        subTotalTextView.text = order.subTotal.toPhilippinesCurrency()
        order.discount.fold({
            vatExemptTextView.text = 0.0.toPhilippinesCurrency()
            discountTextView.text = 0.0.toPhilippinesCurrency()
            totalTextView.text = order.orderLines.list.map { it.basePriceTotal }.sum().toPhilippinesCurrency()
        }, {
            val vatExempted = order.orderLines.list.map { it.vatValue }.sum()
            val discountValue = order.orderLines.list.map { it.discountValue() }.sum()
            vatExemptTextView.text = vatExempted.toPhilippinesCurrency()
            discountTextView.text = discountValue.toPhilippinesCurrency()
            totalTextView.text = order.orderLines.list.map { it.total() }.sum().toPhilippinesCurrency()
        })



        order.hcpRequestNumber.map {
            prescriptionNumberEditText.text = it
        }

        acceptDeliveryButton.text = getString(R.string.pick_up_this_order)
//        acceptDeliveryButton.setOnClickListener {
//            consumerHealthOrderDB.performPickup(consumerOrderId, user, ::displayOrder)
//            activity?.onBackPressed()
//        }
//
//        pickupHoldButton.setOnClickListener {
//            consumerHealthOrderDB.hold(consumerOrderId, user, ::displayOrder)
//            activity?.onBackPressed()
//        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }
}
