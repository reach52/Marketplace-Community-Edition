package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XBeneficiaryItemBinding
import reach52.marketplace.community.insurance.entity.Beneficiary
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

class BeneficiaryListAdapter(
		private val context: Context,
		private val beneficiaryList: ArrayList<Beneficiary>,
		private val simplified: Boolean = false,
		private val onListSizeChange: (size: Int) -> Unit = {}
) : RecyclerView.Adapter<BeneficiaryListAdapter.BeneficiaryItemViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiaryItemViewHolder {
		return BeneficiaryItemViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_beneficiary_item, parent, false)
		)
	}

	override fun onBindViewHolder(holder: BeneficiaryItemViewHolder, position: Int) {
		val beneficiary = beneficiaryList[position]
		holder.setBeneficiary(beneficiary)
		holder.b.beniRemoveBtn.setOnClickListener {
			removeBeneficiary(beneficiary, position)
		}
	}

	override fun getItemCount() = beneficiaryList.size

	fun addBeneficiary(beneficiary: Beneficiary) {

		beneficiaryList.add(beneficiary)
		notifyItemInserted(beneficiaryList.size)
		onListSizeChange(itemCount)

	}

	private fun removeBeneficiary(beneficiary: Beneficiary, position: Int) {
		beneficiaryList.remove(beneficiary)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, itemCount - position)
		onListSizeChange(itemCount)
	}

	inner class BeneficiaryItemViewHolder(val b: XBeneficiaryItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setBeneficiary(beneficiary: Beneficiary) {

			b.beni = beneficiary
			val detailsBuilder = StringBuilder()
			val contactsBuilder = StringBuilder()
			val age = Period.between(beneficiary.DOB.toLocalDate(), LocalDate.now()).years
			detailsBuilder
					.append(context.getString(R.string.gender) + ": ${beneficiary.gender}\n")
					.append(context.getString(R.string.age) + ": $age " + context.getString(R.string.years_old))



			if (simplified) {
				b.beniRemoveBtn.visibility = View.GONE
				contactsBuilder.append(context.getString(R.string.phone) + ": ${beneficiary.phone}")
				b.divider.visibility = View.GONE
			} else {
				contactsBuilder
						.append(context.getString(R.string.phone) + ": ${beneficiary.phone}")
						.append("\n${context.getString(R.string.address)}: ")
						.append("${beneficiary.addressLine1}"+",")
						.append(if (beneficiary.addressLine2.isNotEmpty()) "${beneficiary.addressLine2}"+"," else "")
						.append("${beneficiary.city}"+",")
						.append("${beneficiary.country}"+",")
						.append("${beneficiary.zipCode}")
			}
			b.beniDetails.text = detailsBuilder.toString()
			b.beniContact.text = contactsBuilder.toString()

		}

	}

}