package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XMemberItemBinding
import reach52.marketplace.community.extensions.utils.dip
import reach52.marketplace.community.insurance.entity.Member

class MemberListAdapter(
		private val context: Context,
		private val membersList: ArrayList<Member>,
		private val simplified: Boolean = false,
		private val listChangeListener: ListChangeListener? = null
) : RecyclerView.Adapter<MemberListAdapter.MemberItemViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
		return MemberItemViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_member_item, parent, false)
		)
	}

	fun addMember(member: Member) {

		membersList.add(member)
		notifyItemInserted(membersList.size)
		listChangeListener?.onMemberAdded(itemCount)

	}

	private fun removeMember(member: Member, position: Int) {
		membersList.remove(member)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, itemCount - position)
	}

	override fun getItemCount(): Int {
		return membersList.size
	}

	override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {

		val member = membersList[position]
		holder.setMember(member)
		holder.b.memberRemoveBtn.setOnClickListener {
			removeMember(member, position)
			listChangeListener?.onMemberRemoved(itemCount)
		}

	}

	interface ListChangeListener {
		fun onMemberAdded(listSize: Int)
		fun onMemberRemoved(listSize: Int)
	}

	inner class MemberItemViewHolder(val b: XMemberItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setMember(member: Member) {

			b.member = member

			if (simplified) {
				b.memberRemoveBtn.visibility = View.GONE
				b.memberDetails.text = member.constructParty.party.displayName
				b.divider.visibility = View.GONE
			} else {
				b.memberDetails.text = "${member.DOB.toLocalDate()}, ${member.gender}\n${member.constructParty.party.displayName}"
				if (member.constructParty.party.code == "primary") {
					b.memberRemoveBtn.visibility = View.GONE
					val params = b.divider.layoutParams
					params.height = dip(6)
					b.divider.layoutParams = params
				} else {
					b.memberRemoveBtn.visibility = View.VISIBLE
				}
			}

		}

	}

}