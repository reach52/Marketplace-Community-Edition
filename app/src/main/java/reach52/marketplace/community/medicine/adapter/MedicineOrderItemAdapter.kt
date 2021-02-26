package reach52.marketplace.community.medicine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.ItemMedicineOrderBinding
import reach52.marketplace.community.extensions.utils.getCurrencyString
import reach52.marketplace.community.medicine.entity.MedicineOrder.Item

class MedicineOrderItemAdapter(
        val context: Context,
        val currency: String,
        private val medicineItemList: List<Item>
): RecyclerView.Adapter<MedicineOrderItemAdapter.OrderItemViewHolder>() {

    inner class OrderItemViewHolder(val binding: ItemMedicineOrderBinding): RecyclerView.ViewHolder(binding.root){
        fun setMedicineMedicine(item: Item){
            binding.item = item
            binding.priceTextView.text = getCurrencyString(item.price.toString(), currency)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        return OrderItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_medicine_order, parent, false))
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val medicineItem = medicineItemList[position]
        holder.setMedicineMedicine(medicineItem)
    }

    override fun getItemCount(): Int = medicineItemList.size
}