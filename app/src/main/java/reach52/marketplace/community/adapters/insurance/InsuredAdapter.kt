package reach52.marketplace.community.adapters.insurance

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.policy_owner.PolicyOwner
import kotlinx.android.synthetic.main.x_old_policy_order_item.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@ExperimentalUnsignedTypes
class InsuredAdapter(private val detailClicked: (String) -> Unit) :
        ListAdapter<Entity<PolicyOwner>, InsuredAdapter.ViewHolder>(
                diffCallback()) {

    private var pendingQuestionnaire = ""
    private var pendingIssuance = ""
    private var declined = ""
    private var cancelled = ""
    private var approved = ""

    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<PolicyOwner>>() {
            override fun areItemsTheSame(a: Entity<PolicyOwner>, b: Entity<PolicyOwner>): Boolean = a.id == b.id
            override fun areContentsTheSame(a: Entity<PolicyOwner>, b: Entity<PolicyOwner>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        pendingQuestionnaire = parent.context.getString(R.string.pending_questionnaire)
        pendingIssuance = parent.context.getString(R.string.pending_issuance)
        declined = parent.context.getString(R.string.declined)
        cancelled = parent.context.getString(R.string.cancelled)
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.x_old_policy_order_item, parent, false),
                pendingQuestionnaire,
                pendingIssuance,
                declined,
                cancelled,
                approved
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                display(entity.record, entity.id)
                itemView.setOnClickListener {
                    detailClicked(entity.id)
                }
            }
        }
    }
    class ViewHolder(
            private val view: View,
            private val pendingQuestionnaire: String,
            private val pendingIssuance: String,
            private val declined: String,
            private val cancelled: String,
            private val approved: String
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun display(plan: PolicyOwner, id: String) {

            when(plan.status){
                "pending questionnaire" ->{
                    view.purchased_plan_status.text = pendingQuestionnaire
                    view.purchased_plan_status.setTextColor(Color.parseColor("#A8A9AA"))
                    view.statusIndicator.setBackgroundColor(Color.parseColor("#A8A9AA"))
                }
                "pending issuance" ->{
                    view.purchased_plan_status.text = pendingIssuance
                    view.purchased_plan_status.setTextColor(Color.parseColor("#FFD600"))
                    view.statusIndicator.setBackgroundColor(Color.parseColor("#FFD600"))
                }
                "declined", "cancelled" ->{
                    view.purchased_plan_status.text = if(plan.status == "declined") declined else cancelled
                    view.purchased_plan_status.setTextColor(Color.parseColor("#F44336"))
                    view.statusIndicator.setBackgroundColor(Color.parseColor("#F44336"))
                }
                "approved" -> {
                    view.purchased_plan_status.text = approved
                    view.purchased_plan_status.setTextColor(Color.parseColor("#364FF4"))
                    view.statusIndicator.setBackgroundColor(Color.parseColor("#364FF4"))
                }
                "active" ->{
                    view.validityLabelTextDisplay.visibility = View.VISIBLE
                    val expiry = plan.expiry.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                    val effectiveDate = plan.effectiveDate.plusHours(8).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))

                    view.purchased_plan_status.text = UUID.fromString(id).base58String()
                    view.validityLabelTextDisplay.text = "$effectiveDate - $expiry"

                }
            }

            view.insuredName.text = plan.insurer.insurerName
            view.planName.text = plan.insurer.plan.first().tier

        }
    }
}
