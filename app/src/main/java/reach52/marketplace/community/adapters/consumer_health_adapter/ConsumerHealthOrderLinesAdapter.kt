package reach52.marketplace.community.adapters.consumer_health_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reach52.marketplace.community.R
import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.models.consumer_health_order.OrderLines
import kotlinx.android.synthetic.main.order_item_list_item.view.*

class ConsumerHealthOrderLinesAdapter(orderLineItems: OrderLineItems) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ConsumerHealthOrderLinesAdapter.ViewHolder>(){
    private val items = orderLineItems.list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item_list_item, parent, false))
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    inner class ViewHolder(private val view: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun setData(orderItem: OrderLines) {
            view.ingredientsTextView.text = orderItem.unitOfMeasure
            view.nameTextView.text = orderItem.name
            view.packageSizeTextView.text = orderItem.brand
            view.formTextView.text = orderItem.miscInfo
            view.priceTextView.text = orderItem.price.toString()
            view.quantityTextView.text = orderItem.quantity.toString()
            view.subTotalTextView.text = orderItem.basePriceTotal.toString()
        }
    }
}