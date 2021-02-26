package reach52.marketplace.community.adapters.insurance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.CoverageInsured
import kotlinx.android.synthetic.main.item_beneficiary.view.*

class InsuredBeneficiaryAdapter(private val beneficiaries: List<CoverageInsured>) : RecyclerView.Adapter<InsuredBeneficiaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_beneficiary, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return beneficiaries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beneficiary: CoverageInsured = beneficiaries[position]
        holder.setData(beneficiary)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setData(beneficiary: CoverageInsured) {
            view.beneficiaryTextView.text = beneficiary.beneficiary
            view.beneficiaryRelationTextView.text = beneficiary.relationship
            view.beneficiaryContactTextView.text = beneficiary.dependent
        }
    }
}