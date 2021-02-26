package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XInsurerItemBinding
import reach52.marketplace.community.insurance.entity.Insurer

class InsurerListAdapter(
		val context: Context,
		private val insurerList: ArrayList<Insurer>,
		private val onInsurerSelect: (insurer: Insurer) -> Unit = {}
) : RecyclerView.Adapter<InsurerListAdapter.InsurerItemViewHolder>()
{

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsurerItemViewHolder {
		return InsurerItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_insurer_item, parent, false))
	}

	override fun onBindViewHolder(holder: InsurerItemViewHolder, position: Int) {

		val insurer = insurerList[position]
		holder.setInsurer(insurer)
		holder.itemView.setOnClickListener {
			onInsurerSelect(insurer)
		}

	}

	override fun getItemCount() = insurerList.size


	inner class InsurerItemViewHolder(val b: XInsurerItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setInsurer(insurer: Insurer) {
			b.insurer = insurer
		}

	}

}