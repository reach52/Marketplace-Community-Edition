package reach52.marketplace.community.adapters.medication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.medication.Suppliers
import kotlinx.android.synthetic.main.item_medicine_suppliers.view.*

class MedicationSupplierAdapter(private val onClicked: (Suppliers) -> Unit):
    PagedListAdapter<Entity<Suppliers>, MedicationSupplierAdapter.ViewHolder>(
            diffCallback()
    ){

    companion object{
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<Suppliers>>(){
            override fun areItemsTheSame(a: Entity<Suppliers>, b: Entity<Suppliers>): Boolean = a.id == b.id
            override fun areContentsTheSame(a: Entity<Suppliers>, b: Entity<Suppliers>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_medicine_suppliers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply{
            getItem(position)?.let { entity ->
                display(entity.record)
                itemView.setOnClickListener{ onClicked(entity.record)}
            }
        }
    }

    class ViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun display(suppliers: Suppliers){
            view.suppliersName.text = suppliers.supplierName
        }
    }
}