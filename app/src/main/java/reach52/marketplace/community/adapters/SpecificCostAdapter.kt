package reach52.marketplace.community.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.R.drawable.*
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.SpecificCost
import kotlinx.android.synthetic.main.item_specific_cost.view.*

class SpecificCostAdapter : ListAdapter<SpecificCost, SpecificCostAdapter.ViewHolder>(diffCallback()) {
    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<SpecificCost>() {
            override fun areItemsTheSame(oldItem: SpecificCost, newItem: SpecificCost): Boolean = oldItem.identifier == newItem.identifier
            override fun areContentsTheSame(oldItem: SpecificCost, newItem: SpecificCost): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_specific_cost, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { specificCost ->
                display(specificCost)
            }
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(specificCost: SpecificCost) {
            var setImage = hospitalization

            when (specificCost.identifier) {
                "1" -> {
                    setImage = accidental_death
                }
                "2" -> {
                    setImage = medical_reimbursement
                }
                "3" -> {
                    setImage = hospitalization
                }
                "4" -> {
                    setImage = daily_hospital_cash_for_in_patient
                }
                "5" -> {
                    setImage = emergency_room_cash_for_out_patient
                }
            }

            view.specificCostImage.setImageDrawable(
                    ContextCompat.getDrawable(view.context, setImage)
            )
            view.specificCostTitle.text = specificCost.category
            view.specificCostView.text = specificCost.cost.toPhilippinesCurrency()
        }
    }
}