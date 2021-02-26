package reach52.marketplace.community.adapters.insurance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XDependentItemBinding
import reach52.marketplace.community.models.Dependent

class DependentsAdapter(
		private val context: Context,
		private val dependentsList: ArrayList<Dependent>,
		private val simplifiedView: Boolean = false,
		private val listChangeListener: ListChangeListener? = null
) : RecyclerView.Adapter<DependentsAdapter.DependentItemViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DependentItemViewHolder {
		return DependentItemViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_dependent_item, parent, false)
		)
	}

	fun addDependent(dependent: Dependent) {

		dependentsList.add(dependent)
		notifyItemInserted(dependentsList.size)
		listChangeListener?.onDependentAdded(itemCount)

	}

	override fun getItemCount(): Int {
		return dependentsList.size
	}

	override fun onBindViewHolder(holder: DependentItemViewHolder, position: Int) {

		val dependent = dependentsList[position]
		holder.setDependent(dependent)
		holder.b.dependentRemoveBtn.setOnClickListener {
			dependentsList.remove(dependent)
			notifyItemRemoved(position)
			notifyItemRangeChanged(position, itemCount - position)
			listChangeListener?.onDependentRemoved(itemCount)
		}

	}

	interface ListChangeListener {
		fun onDependentAdded(listSize: Int)
		fun onDependentRemoved(listSize: Int)
	}

	inner class DependentItemViewHolder(val b: XDependentItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setDependent(dependent: Dependent) {

			b.dependentName.text = dependent.fullName
			b.dependentDetails.text = "${dependent.DOB.toLocalDate()}, ${dependent.gender}, ${dependent.relation}"

			if (simplifiedView) {
				b.dependentRemoveBtn.visibility = View.GONE
			}

		}

	}

}