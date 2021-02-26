package reach52.marketplace.community.adapters.insurance_cambodia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.insurance.Formulary
import kotlinx.android.synthetic.main.item_insurance_plan.view.*

class InsurancePlanAdapter (private val onClicked: (Formulary) -> Unit) : PagedListAdapter<Formulary, InsurancePlanAdapter.ViewHolder>(diffCallback()){

    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Formulary>() {
            override fun areItemsTheSame(oldItem: Formulary, newItem: Formulary): Boolean = oldItem.identifier == newItem.identifier
            override fun areContentsTheSame(oldItem: Formulary, newItem: Formulary): Boolean = oldItem == newItem
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(formulary: Formulary) {
            view.insurancePlanName.text = formulary.tier
            view.insuranceDisplayCategory.text = formulary.category
            view.insuranceSummary.text = formulary.summary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_insurance_plan, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let {formulary ->
                display(formulary)
                itemView.setOnClickListener { onClicked(formulary) }
            }
        }
    }

}