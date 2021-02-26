package reach52.marketplace.community.adapters.medication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.medication.Order
import reach52.marketplace.community.models.medication.OrderStatus

class OrderAdapter(private val orderClicked: (String) -> Unit) :
        PagedListAdapter<Entity<Order>, OrderAdapter.ViewHolder>(diffCallback()) {
    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<Order>>() {
            override fun areItemsTheSame(a: Entity<Order>, b: Entity<Order>): Boolean = a.id == b.id
            override fun areContentsTheSame(a: Entity<Order>, b: Entity<Order>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
//                setData(entity.record)
//                itemView.setOnClickListener { orderClicked(entity.id) }
            }
        }
    }

    class ViewHolder(private val view: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        companion object {
            private fun statusColor(status: OrderStatus.Status): Int = when (status) {
                OrderStatus.Status.ACCEPTED -> Color.rgb(0, 132, 255) // Blue
                OrderStatus.Status.DELIVERED -> Color.rgb(102, 51, 0) // Brown
                OrderStatus.Status.DISPATCHED -> Color.rgb(3, 171, 45) // Green
                OrderStatus.Status.NOT_DELIVERED -> Color.rgb(255, 0, 21) // Red
                OrderStatus.Status.PENDING -> Color.rgb(255, 136, 0) // Orange
                OrderStatus.Status.PICKED_UP -> Color.rgb(153, 204, 255) // Light blue
                OrderStatus.Status.REJECTED -> Color.rgb(255, 0, 21) // Red
                OrderStatus.Status.RECEIVED -> Color.rgb(153, 51, 255) // Purple
                OrderStatus.Status.ON_HOLD -> Color.rgb(220, 220, 220) // Grey

            }
        }

//        fun setData(order: Order) {
//            view.orderStatusLabel.setBackgroundColor(statusColor(order.currentStatus.status))
//            view.orderNumberItem.text = order.patientName
//            view.orderResidentAddress.text = order.patientAddress
//            view.medicineCount.text = order.items?.list?.size.toString()
//        }
    }
}
