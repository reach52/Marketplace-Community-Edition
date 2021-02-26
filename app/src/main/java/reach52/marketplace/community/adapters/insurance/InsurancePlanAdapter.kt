package reach52.marketplace.community.adapters.insurance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.InsurancePlan
import kotlinx.android.synthetic.main.item_insurance.view.*

class InsurancePlanAdapter(private val onClicked: (InsurancePlan) -> Unit) :
        PagedListAdapter<InsurancePlan, InsurancePlanAdapter.ViewHolder>(
                diffCallback()
        ) {
    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<InsurancePlan>() {
            override fun areItemsTheSame(a: InsurancePlan, b: InsurancePlan): Boolean = a.identifier == b.identifier
            override fun areContentsTheSame(a: InsurancePlan, b: InsurancePlan): Boolean =
                    a == b
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
            getItem(position)?.let { plan ->
                display(plan)
                itemView.setOnClickListener { onClicked(plan) }
            }
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(insurancePlan: InsurancePlan) {
            view.insuranceName.text = insurancePlan.title
        }
    }
}