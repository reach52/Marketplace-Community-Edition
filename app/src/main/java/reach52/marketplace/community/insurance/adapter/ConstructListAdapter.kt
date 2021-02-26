package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XConstructItemBinding
import reach52.marketplace.community.insurance.entity.InsuranceProduct

class ConstructListAdapter(
		private val context: Context,
		private val constructs: ArrayList<InsuranceProduct.Construct>,
		private val onConstructSelect: (construct: InsuranceProduct.Construct) -> Unit = {}
) : RecyclerView.Adapter<ConstructListAdapter.ConstructViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstructViewHolder {
		return ConstructViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_construct_item, parent, false)
		)
	}

	override fun onBindViewHolder(holder: ConstructViewHolder, position: Int) {
		val c = constructs[position]
		holder.setConstruct(c)
		holder.itemView.setOnClickListener {
			onConstructSelect(c)
		}
	}

	override fun getItemCount() = constructs.size

	inner class ConstructViewHolder(
			val b: XConstructItemBinding
	) : RecyclerView.ViewHolder(b.root) {
		fun setConstruct(construct: InsuranceProduct.Construct) {
			b.construct = construct
			val period = construct.premium.keys.toList()[0]
			val amount = construct.premium[period]!!.amount.toInt()
			b.consPermium.text = String.format(context.getString(R.string.premium_starting_form), amount.toString(), construct.isoCurrency.toUpperCase())
		}
	}

}