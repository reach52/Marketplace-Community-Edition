package reach52.marketplace.community.fragments.medication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import arrow.core.getOrElse
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.models.medication.Order
import reach52.marketplace.community.models.medication.Orders
import reach52.marketplace.community.persistence.DispergoDatabase
import kotlinx.android.synthetic.main.fragment_on_hold_orders_details.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@ExperimentalUnsignedTypes
class OnHoldOrdersDetailsFragment : androidx.fragment.app.Fragment() {

    companion object {
        private const val KEY_ORDER_ID = "orderId"

        fun newInstance(orderId: String): OnHoldOrdersDetailsFragment {
            return OnHoldOrdersDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ORDER_ID, orderId)
                }
            }
        }
    }

    private val orderId by lazy {
        arguments?.getString(KEY_ORDER_ID)!!
    }

    private val orderIdBase58 by lazy {
        UUID.fromString(orderId).base58String()
    }

    private val orders by lazy {
        Orders(DispergoDatabase.getInstance(context!!))
    }

//    private val user by lazy {
//        Auth.currentSession(context!!).map {
//            it.user
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.on_hold_details)
        return inflater.inflate(R.layout.fragment_on_hold_orders_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        orderItemsView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        displayOrder(orders.get(orderId))
    }

    private fun displayOrder(order: Order) {
        trackingCodeTextView.text = orderIdBase58
        patientNameTextView.text = order.patientName
        physicianNameTextView.text = order.physicianName
        orderStatusTextView.text = order.currentStatus.status.toString()
        orderDateTextView.text = order.currentStatus.statusDate.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
//        orderItemsView.adapter = OrderItemAdapter(order.items)
        rejectReasonTextView.text = order.currentStatus.reason.getOrElse { getString(R.string.none) }

        discountIdNumberTextView.text = order.discountIdNumber.getOrElse { "none" }
//        subTotalTextView.text = order.subTotal.toPhilippinesCurrency()
//        order.discount.fold({
//            vatExemptTextView.text = 0.0.toPhilippinesCurrency()
//            discountTextView.text = 0.0.toPhilippinesCurrency()
//            feeTextView.text = 24.0.toPhilippinesCurrency()
//            totalTextView.text = (order.items.list.map { it.basePriceTotal }.sum() + 24.00).toPhilippinesCurrency()
//        }, {
//            val vatExempted = order.items.list.map { it.vatValue }.sum()
//            val discountValue = order.items.list.map { it.discountValue() }.sum()
//            val fee = 24.00
//            vatExemptTextView.text = vatExempted.toPhilippinesCurrency()
//            discountTextView.text = discountValue.toPhilippinesCurrency()
//            feeTextView.text = fee.toPhilippinesCurrency()
//            totalTextView.text = (order.items.list.map { it.total() }.sum() + 24.00).toPhilippinesCurrency()
//        })


        order.prescriptionNumber.map {
            prescriptionNumberEditText.text = it
        }

        order.recipient.map {
            recipientNameTextView.text = it
        }

        recipientInfo.visibility = View.GONE
        recipientNameLabelTextView.visibility = View.GONE
        recipientNameTextView.visibility = View.GONE
        resumeButton.isEnabled = true
        resumeButton.visibility = View.VISIBLE

        order.currentStatus.reason.fold({
            rejectReasonTextView.text = getString(R.string.no_reason)
        }, { r ->
            rejectReasonTextView.text = r
        })

        resumeButton.setOnClickListener {
            disableButtons()
//            when (order.pastStatuses.last().status) {
//                OrderStatus.Status.DISPATCHED -> orders.dispatched(orderId, user, ::displayOrder)
//                OrderStatus.Status.RECEIVED -> orders.receive(orderId, user, ::displayOrder)
//                OrderStatus.Status.ACCEPTED -> orders.accept(orderId, user, ::displayOrder)
//                else -> {
//                    // NOOP
//                }
//            }
            activity?.onBackPressed()
        }
    }

    private fun disableButtons() {
        resumeButton.isEnabled = false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.app_bar_search).isVisible = false
    }
}
