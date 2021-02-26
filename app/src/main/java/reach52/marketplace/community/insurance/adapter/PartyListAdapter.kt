package reach52.marketplace.community.insurance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.XPartyListItemBinding
import reach52.marketplace.community.insurance.entity.PolicyOrder

class PartyListAdapter(
		val context: Context,
		val parties: List<PolicyOrder.Party>
) : RecyclerView.Adapter<PartyListAdapter.PartyViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyListAdapter.PartyViewHolder {
		return PartyViewHolder(
				DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.x_party_list_item, parent, false)
		)
	}

	override fun onBindViewHolder(holder: PartyListAdapter.PartyViewHolder, position: Int) {
		holder.setParty(parties[position])
	}

	override fun getItemCount(): Int {
		return parties.size
	}

	inner class PartyViewHolder(val b: XPartyListItemBinding) : RecyclerView.ViewHolder(b.root) {

		fun setParty(party: PolicyOrder.Party) {
			b.party = party
		}

	}

}