package reach52.marketplace.community.medicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import reach52.marketplace.community.databinding.XDiscountItemBinding
import reach52.marketplace.community.medicine.entity.Discount

class DiscountsListAdapter(context: Context, private val discounts: ArrayList<Discount>) : ArrayAdapter<Discount>(context, 0, discounts){

	@SuppressLint("ViewHolder")
	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

		val b = XDiscountItemBinding.inflate(LayoutInflater.from(context), parent, false)

		val discount = discounts[position]
		b.discount = discount

		return b.root
	}

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
		return getView(position, convertView, parent)
	}
}