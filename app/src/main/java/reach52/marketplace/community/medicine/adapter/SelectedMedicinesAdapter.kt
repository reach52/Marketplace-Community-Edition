package reach52.marketplace.community.medicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.SelectedMedicationsListItemBinding
import reach52.marketplace.community.extensions.utils.getCurrencyString
import reach52.marketplace.community.medicine.entity.SelectedMedicine
import kotlinx.android.synthetic.main.selected_medications_list_item.view.*

class SelectedMedicinesAdapter(
		val context: Context,
		val selectedMedsList: ArrayList<SelectedMedicine>,
		val listUpdated: () -> Unit = { }
) : RecyclerView.Adapter<SelectedMedicinesAdapter.SelectedMedicationViewHolder>() {

	override fun getItemCount(): Int {

		return selectedMedsList.size
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMedicationViewHolder {
		return SelectedMedicationViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.selected_medications_list_item, parent, false))
	}

	fun deleteItem(pos: Int) {
		showRemoveDialog(pos)
	}

	@SuppressLint("CheckResult")
	override fun onBindViewHolder(holder: SelectedMedicationViewHolder, position: Int) {

		//groupitems
		//selectedMedsList.sortedByDescending { car -> car.medicine.supplierCode }
//		selectedMedsList.groupingBy { car -> car.medicine.supplierCode }

		val med = selectedMedsList[position]
		holder.setData(med)

		holder.b.removeBtn.setOnClickListener {
			showRemoveDialog(position)
		}

		holder.itemView.quantityTextInputEditText.textChanges().subscribe({

			try {
				med.qty = it.toString().toInt()
				if (med.qty == 0) {
					showRemoveDialog(position) {
						holder.itemView.quantityTextInputEditText.setText("1")
					}
				} else {
					listUpdated()
				}
			} catch (e: Exception) {
				Log.e("taaag","error parsing it")
			}

		}, {

		})
//		var qtyString = holder.itemView.quantityTextInputEditText.text.toString()
//		var qty = 0;
//		if (qtyString != "") {
//			qty = holder.itemView.quantityTextInputEditText.text.toString().toInt()
//		} else if (med.qty != null) {
//			qty = med.qty
//		}
//
		holder.b.btnPlus.setOnClickListener {

			med.qty++
			holder.itemView.quantityTextInputEditText.setText(med.qty.toString())

		}
		holder.b.btnMinus.setOnClickListener {

			if (med.qty > 1) {
				med.qty--
				holder.itemView.quantityTextInputEditText.setText(med.qty.toString())
			} else {
				showRemoveDialog(position)
			}


		}


	}

	private fun showRemoveDialog(position: Int, onCancel: () -> Unit = {}) {
		AlertDialog.Builder(context).apply {
			setMessage(R.string.remove_item_prompt)
					.setPositiveButton(R.string.remove) { _, _ ->
						removeMedication(position)
					}
					.setNegativeButton(R.string.cancel) { dialog, _ ->
						onCancel()
						dialog.cancel()
					}
					.setOnCancelListener {
						onCancel()
						it.cancel()
					}
		}.create().show()
	}

//    fun addMedication(medication: Medicine) {
//
//        val med = selectedMedsList.firstOrNull {
//            medication.id == it.medicine.id
//        }
//
//        if (med == null) {
//            val selectedMed = SelectedMedicine(medication, 1)
//            selectedMedsList.add(selectedMed)
//            notifyItemInserted(selectedMedsList.size - 1)
//            listUpdated()
//        }
//
//    }

	fun removeMedication(position: Int) {
		selectedMedsList.removeAt(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, selectedMedsList.size)
		listUpdated()

	}

//    fun orderItems(): OrderItems {
//        return OrderItems.fromList(orderItems.map {
//
//            MedicinePurchase.Item(
//                    dosage = it.medication.dosage,
//                    form = it.medication.form,
//                    medicationId = it.medication.id,
//                    brandName = it.medication.brandName,
//                    price = it.medication.price,
//                    qty = it.quantity,
//                    tax = it.medication.tax.isIncluded.to,
//                    description = it.medication.description
//            )
//        })
//    }

//    private fun total() = orderItems.map {
//        it.medication.price * it.quantity
//    }.sum()

//    class SelectedMedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun setData(orderItemViewModel: OrderItemViewModel) {
//            itemView.apply {
//                val medication = orderItemViewModel.medication
//                dosageTextView.text = medication.dosage
//                formTextView.text = medication.form
////                ingredientsTextView.text = medication.ingredients.joinToString(" + ")
//                nameTextView.text = medication.brandName
////                packageSizeTextView.text = medication.packageSize.toString()
//                priceTextView.text = medication.price.toPhilippinesCurrency()
//                quantityTextInputEditText.setText(
//                        orderItemViewModel.quantity.toString(),
//                        TextView.BufferType.EDITABLE
//                )
////                val medication = orderItemViewModel.medication
////                dosageTextView.text = medication.dosages.toString()
////                formTextView.text = medication.forms.toString()
////                ingredientsTextView.text = medication.ingredients.joinToString(" + ")
////                nameTextView.text = medication.names.toString()
////                packageSizeTextView.text = medication.packageSize.toString()
////                priceTextView.text = medication.price.toPhilippinesCurrency()
////                quantityTextInputEditText.setText(
////                        orderItemViewModel.quantity.toString(),
////                        TextView.BufferType.EDITABLE
////                )
//            }
//        }
//    }

	inner class SelectedMedicationViewHolder(val b: SelectedMedicationsListItemBinding) : RecyclerView.ViewHolder(b.root) {
		fun setData(medicine: SelectedMedicine) {
			b.selectedMedicine = medicine
			if (medicine.medicine.manufacturer.isEmpty()) {
				b.brandTextView.visibility = View.GONE
			}
			b.priceTextView.text = getCurrencyString(medicine.medicine.price.toString(), medicine.medicine.isoCurrency)
		}
	}


}



