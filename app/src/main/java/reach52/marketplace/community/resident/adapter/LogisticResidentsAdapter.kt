package reach52.marketplace.community.resident.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XLogisticResidentListItemBinding
import reach52.marketplace.community.resident.entity.Resident

class LogisticResidentsAdapter(
		private val context: Context,
		private val onItemClick: (resident: Resident) -> Unit = {}
) : RecyclerView.Adapter<LogisticResidentsAdapter.ViewHolder>() {

	var residentsList = ArrayList<Resident>()
		set(list) {
			field = list
			notifyDataSetChanged()
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_logistic_resident_list_item, parent, false)
		)
	}

	override fun getItemCount(): Int {
		return residentsList.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {

		val resident = residentsList[position]
		holder.setResident(resident)
		holder.itemView.setOnClickListener {
			onItemClick(resident)
		}

	}

	class ViewHolder(val b: XLogisticResidentListItemBinding) : RecyclerView.ViewHolder(b.root) {
		fun setResident(resident: Resident) {
			b.resident = resident
		}
	}


}