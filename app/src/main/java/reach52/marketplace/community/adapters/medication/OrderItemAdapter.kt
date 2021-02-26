package reach52.marketplace.community.adapters.medication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import reach52.marketplace.community.R
import reach52.marketplace.community.models.medication.OrderItem
import reach52.marketplace.community.models.medication.OrderItems
import kotlinx.android.synthetic.main.order_item_list_item.view.*

class OrderItemAdapter(orderItems: OrderItems) :
        androidx.recyclerview.widget.RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private val items = orderItems.list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item_list_item, parent, false))
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.setData(items[position])
    }

    inner class ViewHolder(private val view: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun setData(orderItem: OrderItem) {
            view.ingredientsTextView.text = orderItem.medicationIngredients.joinToString("+")
            view.nameTextView.text = orderItem.medicationName
            view.packageSizeTextView.text = orderItem.medicationPackageSize.toString()
            view.dosageTextView.text = orderItem.medicationDosage
            view.formTextView.text = orderItem.medicationForm
            view.priceTextView.text = orderItem.price.toString()
            view.quantityTextView.text = orderItem.quantity.toString()
            view.subTotalTextView.text = orderItem.basePriceTotal.toString()
        }
    }
}