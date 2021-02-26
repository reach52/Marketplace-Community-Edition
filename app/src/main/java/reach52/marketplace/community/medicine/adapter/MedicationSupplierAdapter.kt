package reach52.marketplace.community.medicine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.ItemMedicineSuppliersBinding
import reach52.marketplace.community.medicine.entity.Supplier


class MedicationSupplierAdapter(
        val context: Context,
        private val supplierList: ArrayList<Supplier>,
        var listChangeListener: MedicationSupplierAdapter.Onclick,
       // private val onSupplierSelect: (supplier: Supplier) -> Unit = {},

        ): RecyclerView.Adapter<MedicationSupplierAdapter.SupplierItemViewHolder>()
{
    val selectSupplierList = ArrayList<Supplier>()
    var asd = 0
    var isAllSelected : Boolean = false;
    val onSupplierSelectmulti: (supplierList: ArrayList<Supplier>)-> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierItemViewHolder {
        return SupplierItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_medicine_suppliers, parent, false))
    }

    override fun onBindViewHolder(holder: SupplierItemViewHolder, position: Int) {
        val supplier = supplierList[position]
        holder.setSupplier(supplier)
        holder.itemView.setOnClickListener{
           // onSupplierSelect(supplier)

        }
        // Update list

        if (isAllSelected)
        {
            for (i in 0 until supplierList!!.size) {
                supplierList.get(i).status = 1
                holder.b.checkBox.isChecked  = true

            }
            listChangeListener?.onItemChecked()
        }
        else
        {
            for (i in 0 until supplierList!!.size) {
                supplierList.get(i).status = 0
                holder.b.checkBox.isChecked  = false
            }
            listChangeListener?.onItemChecked()
        }

        holder.b.checkBox.setOnClickListener({
            if (!holder.b.checkBox.isChecked) {
                supplier.status = 0;
            } else if (holder.b.checkBox.isChecked) {
                supplier.status = 1
            }
            listChangeListener?.onItemChecked()
        })

    }

    override fun getItemCount() = supplierList.size

    inner class SupplierItemViewHolder(val b: ItemMedicineSuppliersBinding) :
            RecyclerView.ViewHolder(b.root){

        fun setSupplier(supplier: Supplier) {
            b.supplier = supplier
        }


    }

    fun itemSelected(isAllSelected: Boolean)
    {
        this.isAllSelected = isAllSelected;
        notifyDataSetChanged()

    }

    interface Onclick {
        fun onItemChecked()
    }
}


