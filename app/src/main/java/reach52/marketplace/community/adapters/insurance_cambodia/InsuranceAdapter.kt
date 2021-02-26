package reach52.marketplace.community.adapters.insurance_cambodia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.insurance.InsurerDocument
import kotlinx.android.synthetic.main.item_insurance_cambodia.view.*

class InsuranceAdapter(private val onClicked: (String, InsurerDocument) -> Unit) : PagedListAdapter<Entity<InsurerDocument>, InsuranceAdapter.ViewHolder>(diffCallback()) {

    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<InsurerDocument>>() {
            override fun areItemsTheSame(oldItem: Entity<InsurerDocument>, newItem: Entity<InsurerDocument>): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Entity<InsurerDocument>, newItem: Entity<InsurerDocument>): Boolean = oldItem.record == newItem.record
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(document: InsurerDocument) {
            view.insuranceName.text = document.display
            view.insuranceDescription.text = document.summary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_insurance_cambodia, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                display(entity.record)
                itemView.setOnClickListener { onClicked(entity.id, entity.record) }
            }
        }
    }
}