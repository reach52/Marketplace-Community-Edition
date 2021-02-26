package reach52.marketplace.community.adapters.insurance_cambodia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.toCambodiaCurrency
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.insurance.Benefit
import kotlinx.android.synthetic.main.item_benefit.view.*
import java.util.*

class BenefitAdapter : ListAdapter<Benefit, BenefitAdapter.ViewHolder>(diffCallBack()){

    companion object {
        fun diffCallBack() = object : DiffUtil.ItemCallback<Benefit>(){
            override fun areItemsTheSame(oldItem: Benefit, newItem: Benefit): Boolean = oldItem.identifier == newItem.identifier
            override fun areContentsTheSame(oldItem: Benefit, newItem: Benefit): Boolean = oldItem == newItem
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(benefit: Benefit){
            var amount : String
            amount = if (Locale.getDefault().displayLanguage == "English"){
                benefit.amount.toPhilippinesCurrency()
            } else {
                benefit.amount.toCambodiaCurrency()
            }

            if(benefit.amount == 0.0){
                amount = "Full Cost"
            }
            view.benefitNameTextView.text = benefit.category
            view.benefitPriceTextView.text = amount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_benefit, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let {
                display(it)
            }
        }
    }
}