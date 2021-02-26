package reach52.marketplace.community.adapters.medication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Try
import arrow.core.firstOrNone
import com.jakewharton.rxbinding3.view.longClicks
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.medicine.entity.Medicine
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.selected_medications_list_item.view.*

class SelectedMedicationAdapter :
        RecyclerView.Adapter<SelectedMedicationAdapter.SelectedMedicationViewHolder>() {
    companion object {
        private const val TAG = "SelectedMedAdapter"
    }

    private val totalEmitter: PublishSubject<Double> = PublishSubject.create()
    private val disposables = CompositeDisposable()
    private val orderItems: MutableList<OrderItemViewModel> = mutableListOf()

    val orderTotal: Observable<Double> = totalEmitter.startWith(0.00)

    override fun getItemCount(): Int {
        return orderItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMedicationViewHolder {
        return SelectedMedicationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.selected_medications_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: SelectedMedicationViewHolder, position: Int) {
        disposables.add(holder.itemView.longClicks { true }.subscribe({
            AlertDialog.Builder(holder.itemView.context).apply {
                setMessage(R.string.remove_item_prompt).setPositiveButton(R.string.remove) { _, _ ->
                    orderItems.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    totalEmitter.onNext(total())
                }.setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }.create().show()
        }, {
            Log.w(TAG, "longClicks", it)
        }))

        disposables.add(holder.itemView.quantityTextInputEditText.textChanges().subscribe({
            Try { it.toString().toInt() }.map {
                orderItems[holder.adapterPosition].quantity = it
                totalEmitter.onNext(total())
            }
        }, {
            Log.w(TAG, "textChanges", it)
        }))

        holder.setData(orderItems[position])
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposables.clear()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun addMedication(medication: Medicine) {
        orderItems.firstOrNone { item -> item.medication.id == medication.id }.fold({
            orderItems.add(OrderItemViewModel(medication, 1))
            notifyItemInserted(orderItems.size - 1)
            totalEmitter.onNext(total())
        }, {
            it.quantity++
        })
    }

//    fun orderItems(): OrderItems {
//        return OrderItems.fromList(orderItems.map {
//
//            OrderItem(
//                    medicationDosage = it.medication.dosage,
//                    medicationForm = it.medication.form,
//                    medicationId = it.medication.id,
//                    medicationName = it.medication.brandName,
//                    price = it.medication.price,
//                    quantity = it.quantity
//
//            )
//        })
//    }

    private fun total() = orderItems.map {
        it.medication.price * it.quantity
    }.sum()

    class SelectedMedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(orderItemViewModel: OrderItemViewModel) {
            itemView.apply {
                val medication = orderItemViewModel.medication

//                ingredientsTextView.text = medication.ingredients.joinToString(" + ")
                nameTextView.text = medication.brandName
//                packageSizeTextView.text = medication.packageSize.toString()
                priceTextView.text = medication.price.toPhilippinesCurrency()
                quantityTextInputEditText.setText(
                        orderItemViewModel.quantity.toString(),
                        TextView.BufferType.EDITABLE
                )
//                val medication = orderItemViewModel.medication
//                dosageTextView.text = medication.dosages.toString()
//                formTextView.text = medication.forms.toString()
//                ingredientsTextView.text = medication.ingredients.joinToString(" + ")
//                nameTextView.text = medication.names.toString()
//                packageSizeTextView.text = medication.packageSize.toString()
//                priceTextView.text = medication.price.toPhilippinesCurrency()
//                quantityTextInputEditText.setText(
//                        orderItemViewModel.quantity.toString(),
//                        TextView.BufferType.EDITABLE
//                )
            }
        }
    }

    data class OrderItemViewModel(val medication: Medicine, var quantity: Int)
}