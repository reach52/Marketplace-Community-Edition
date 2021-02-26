package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.databinding.XBenefitItemBinding
import reach52.marketplace.community.insurance.entity.InsuranceProduct

class BenefitsAdapter(
		val context: Context,
		val benefits: ArrayList<InsuranceProduct.Benefit>
) : RecyclerView.Adapter<BenefitsAdapter.BenefitItemViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitsAdapter.BenefitItemViewHolder {
		return BenefitItemViewHolder(XBenefitItemBinding.inflate(LayoutInflater.from(context), parent, false))
	}

	override fun onBindViewHolder(holder: BenefitsAdapter.BenefitItemViewHolder, position: Int) {
		holder.setBenefit(benefits[position])
	}

	override fun getItemCount() = benefits.size

	inner class BenefitItemViewHolder(val b: XBenefitItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setBenefit(benefit: InsuranceProduct.Benefit) {
			b.benefit = benefit
			b.benefitAmount.text = benefit.totalInsured
			if (benefit.exclusions.trim().isEmpty()) {
				b.exclusionHeading.visibility = View.GONE
				b.benefitExclusions.visibility = View.GONE
			}
			if (benefit.displayDesc.isEmpty()) {
				b.benefitDesc.visibility = View.GONE
			}
		}

	}

}