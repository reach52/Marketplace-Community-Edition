package reach52.marketplace.community.adapters.insurance

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.Insurance
import reach52.marketplace.community.models.InsuranceDomainResource
import kotlinx.android.synthetic.main.item_insurance.view.*

class InsuranceAdapter(private val onClicked: (String, InsuranceDomainResource) -> Unit) :
        PagedListAdapter<Entity<Insurance>, InsuranceAdapter.ViewHolder>(
                diffCallback()
        ) {
    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<Insurance>>() {
            override fun areItemsTheSame(a: Entity<Insurance>, b: Entity<Insurance>): Boolean = a.id == b.id
            override fun areContentsTheSame(a: Entity<Insurance>, b: Entity<Insurance>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_insurance, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                display(entity.record)
                itemView.setOnClickListener { onClicked(entity.id, entity.record.domainResource) }
            }
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(insurance: Insurance) {
            view.insuranceName.text = insurance.domainResource.name
            Log.d("aron", insurance.domainResource.name)
        }
    }
}