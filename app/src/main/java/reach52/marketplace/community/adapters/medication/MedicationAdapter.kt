package reach52.marketplace.community.adapters.medication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.fragments.medication.MedicineDetailsFragment
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.models.Entity
import kotlinx.android.synthetic.main.search_medication_list_item.view.*

class MedicationAdapter(private val receiver: (Medicine) -> Unit) :
        PagedListAdapter<Entity<Medicine>, MedicationAdapter.ViewHolder>(diffCallback()) {
    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<Medicine>>() {
            override fun areItemsTheSame(a: Entity<Medicine>, b: Entity<Medicine>): Boolean =
                    a.id == b.id

            override fun areContentsTheSame(a: Entity<Medicine>, b: Entity<Medicine>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_medication_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                setData(entity.record)
                itemView.setOnClickListener { receiver(entity.record) }
            }
        }
    }

    class ViewHolder(private val view: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun setData(medication: Medicine) {
//            view.dosageTextView.text = medication.dosages.toString()
//            view.formTextView.text = medication.forms.toString()
//            view.ingredientsTextView.text = medication.ingredients.joinToString(" + ")
//            view.nameTextView.text = medication.names.toString()
//            view.packageSizeTextView.text = medication.packageSize.toString()
//            view.priceTextView.text = medication.price.toPhilippinesCurrency()
//            view.vatTextView.text = itemView.context.getString(R.string.vat_exclusive)
//
//            when (!medication.isVatExclusive) {
//                true -> view.vatTextView.text = itemView.context.getString(R.string.vat_inclusive)
//            }

            view.dosageTextView.text = medication.dosage
            view.formTextView.text = medication.form
//            view.ingredientsTextView.text = medication.ingredients.joinToString(" + ")
            view.nameTextView.text = medication.brandName
//            view.packageSizeTextView.text = medication.packageSize.toString()
            view.priceTextView.text = medication.price.toPhilippinesCurrency()
            view.vatTextView.text = itemView.context.getString(R.string.vat_exclusive)

//            when (!medication.tax) {
//                true -> view.vatTextView.text = itemView.context.getString(R.string.vat_inclusive)
//            }

            view.detailsButton.setOnClickListener {
                val appCompatActivity = it.context as AppCompatActivity
                appCompatActivity.supportFragmentManager.
                beginTransaction()
                        .replace(R.id.fragment_container, MedicineDetailsFragment())
                        .addToBackStack(null)
                        .commit()
            }

        }
    }
}