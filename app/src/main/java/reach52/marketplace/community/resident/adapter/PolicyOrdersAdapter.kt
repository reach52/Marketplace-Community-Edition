package reach52.marketplace.community.resident.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.databinding.XPolicyOrderItemBinding
import reach52.marketplace.community.insurance.entity.PolicyOrder

class PolicyOrdersAdapter
(
		val context: Context,
		val orders: ArrayList<PolicyOrder>,
		val onOrderSelect: (order: PolicyOrder) -> Unit = {}
) : RecyclerView.Adapter<PolicyOrdersAdapter.OrderViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
		return OrderViewHolder(
				XPolicyOrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
		)
	}

	override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
		val order = orders[position]
		holder.setOrder(order)
		holder.itemView.setOnClickListener {
			onOrderSelect(order)
		}
	}

	override fun getItemCount() = orders.size

	inner class OrderViewHolder(
			val b: XPolicyOrderItemBinding
	) : RecyclerView.ViewHolder(b.root) {

		fun setOrder(order: PolicyOrder) {
			b.order = order
		}

	}

}