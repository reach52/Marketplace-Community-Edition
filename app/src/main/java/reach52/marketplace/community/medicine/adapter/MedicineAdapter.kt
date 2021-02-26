package reach52.marketplace.community.medicine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.ItemMedicineBinding
import reach52.marketplace.community.databinding.LayoutMedicineDetailsBinding
import reach52.marketplace.community.extensions.utils.getCurrencyString
import reach52.marketplace.community.medicine.entity.Medicine


class MedicineAdapter(
		val context: Context,
		private val medicineList: ArrayList<Medicine>,
		private val onMedicineSelect: (medicine: Medicine, qty: Int) -> Unit = { medicine: Medicine, i: Int -> }
) : RecyclerView.Adapter<MedicineAdapter.MedicineItemViewHolder>() {

	init{
		setHasStableIds(true)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineItemViewHolder {
		return MedicineItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_medicine, parent, false))
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemViewType(position: Int): Int {
		return position
	}

	override fun onBindViewHolder(holder: MedicineItemViewHolder, position: Int) {

		val medicine = medicineList[position]

		holder.setMedicine(medicine)
		holder.itemView.setOnClickListener {
			val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
			val binding: LayoutMedicineDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_medicine_details, null, false)
			binding.medicine = medicine
			binding.priceTextView.text = getCurrencyString(medicine.price.toString(), medicine.isoCurrency.toUpperCase())
			if (!medicine.prescriptionRequired) {
				binding.prescriptionReqTextView.visibility = View.GONE
			}
			dialogBuilder.setView(binding.root)
			val alertDialog: AlertDialog = dialogBuilder.create()
			alertDialog.show()
			var qty = 1

			binding.edQty.textChanges().subscribe({
				try {
					qty = it.toString().toInt()
				} catch (e: Exception) {

				}
			}, {


			})

			binding.btnPlus.setOnClickListener {

				if (qty > 0) {
					qty++
					binding.edQty.setText(qty.toString(), TextView.BufferType.EDITABLE)
				}
			}
			binding.btnMinus.setOnClickListener {

				if (qty > 1) {
					qty--
					// binding.edQty.text = qty.toString()
					binding.edQty.setText(qty.toString(), TextView.BufferType.EDITABLE)
				}

			}
			binding.addMedicine.setOnClickListener {
				onMedicineSelect(medicine, qty)
				alertDialog.dismiss()

			}
			binding.cancelAction.setOnClickListener {
				alertDialog.dismiss()
			}
		}

	}

	override fun getItemCount() = medicineList.size

	class MedicineItemViewHolder(val binding: ItemMedicineBinding) : RecyclerView.ViewHolder(binding.root) {
		fun setMedicine(medicine: Medicine) {
			binding.medicine = medicine
			if (!medicine.prescriptionRequired) {
				binding.prescriptionReqTextView.visibility = View.GONE
			}
			binding.priceTextView.text = getCurrencyString(medicine.price.toString(), medicine.isoCurrency)
		}
	}

}