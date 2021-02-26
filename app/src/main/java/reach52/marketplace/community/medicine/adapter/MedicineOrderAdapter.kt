package reach52.marketplace.community.medicine.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.ItemOrderBinding
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.extensions.utils.getCurrencyString
import reach52.marketplace.community.medicine.entity.MedicineOrder
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MedicineOrderAdapter(
        val context: Context,
        private val orderList: ArrayList<MedicineOrder>,
        private val onOrderSelect: (medicineOrder: MedicineOrder) -> Unit = {}
) : RecyclerView.Adapter<MedicineOrderAdapter.MedicineOrderItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineOrderItemViewHolder {
        return MedicineOrderItemViewHolder(
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order, parent, false))
    }

    override fun onBindViewHolder(holder: MedicineOrderItemViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val order = orderList[position]
        holder.setOrder(order)
        holder.itemView.setOnClickListener {
            onOrderSelect(order)
        }
    }

    override fun getItemCount() = orderList.size

    inner class MedicineOrderItemViewHolder(val b: ItemOrderBinding) : RecyclerView.ViewHolder(b.root){
        @ExperimentalUnsignedTypes
        fun setOrder(medicineOrder: MedicineOrder){

            // not sure if this is necessary? need to get feedback about this
            var colorValue = 0

            when(medicineOrder.currentStatus.toUpperCase()){
                "ACCEPTED" -> colorValue = Color.rgb(0, 132, 255)
                "DELIVERED" -> colorValue = Color.rgb(102, 51, 0)
                "DISPATCHED" -> colorValue = Color.rgb(3, 171, 45)
                "NOT_DELIVERED" -> colorValue = Color.rgb(255, 0, 21)
                "PENDING" -> colorValue = Color.rgb(255, 136, 0)
                "PICKED_UP" -> colorValue = Color.rgb(153, 204, 255)
                "REJECTED" -> colorValue = Color.rgb(255, 0, 21)
                "RECEIVED" -> colorValue = Color.rgb(153, 51, 255)
                "ON_HOLD" -> colorValue = Color.rgb(220, 220, 220)
            }

          //  b.statusIndicator.setBackgroundColor(colorValue)
           // b.status.setTextColor(colorValue)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                b.divider1.background.setTint(colorValue)
            }

            val orders = ArrayList<String>()
            medicineOrder.items.forEach{
                orders += it.brandName
            }
            val brandNames = orders.joinToString(
                    prefix = "",
                    separator = ",\n",
                    postfix = "",
                    limit = 3,
                    truncated = "..."
            )
            b.trackingCode.text = UUID.fromString(medicineOrder.id).base58String()
            b.status.text = medicineOrder.currentStatus.toUpperCase()
            b.brandName.text = brandNames
            b.totalCost.text = getCurrencyString(medicineOrder.total.toString(), medicineOrder.isoCurrency)
            b.dateTextView.text = medicineOrder.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))

        }
    }
}