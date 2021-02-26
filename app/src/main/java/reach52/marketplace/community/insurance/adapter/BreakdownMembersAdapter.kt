package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XBreakdownMemberItemBinding
import reach52.marketplace.community.insurance.entity.Member

class BreakdownMembersAdapter (
		val context: Context,
		val members: ArrayList<Member>,
		val curreny: String,
		var period: String
) : RecyclerView.Adapter<BreakdownMembersAdapter.MemberItemViewHolder>(){

	fun changePeriod(period: String) {
		this.period = period
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
		return MemberItemViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_breakdown_member_item, parent, false)
		)
	}

	override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {
		holder.setMember(members[position])
	}

	override fun getItemCount() = members.size

	inner class MemberItemViewHolder(val b: XBreakdownMemberItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setMember(member: Member) {

			b.memberName.text = "${member.firstName} ${member.lastName}"
			b.memberType.text = member.constructParty.party.displayName

			val code = member.constructParty.party.code
			val amount = member.constructParty.party.premium[period]!!.amount

			if (code == "primary" || amount <= 0F) {
				b.permiumAmount.visibility = View.INVISIBLE
			} else {
				b.permiumAmount.text = "$amount ${curreny}"
			}

		}

	}

}