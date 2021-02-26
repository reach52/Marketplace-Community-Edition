package reach52.marketplace.community.adapters.follow_up

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.follow_up.FollowUp
import kotlinx.android.synthetic.main.item_all_follow_up.view.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class AllFollowUpAdapter (private val detailClicked: (String) -> Unit) : ListAdapter<Entity<FollowUp>, AllFollowUpAdapter.ViewHolder>(diffCallBack()){

    companion object {
        fun diffCallBack() = object : DiffUtil.ItemCallback<Entity<FollowUp>>() {
            override fun areItemsTheSame(oldItem: Entity<FollowUp>, newItem: Entity<FollowUp>):
                    Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Entity<FollowUp>, newItem: Entity<FollowUp>):
                    Boolean = oldItem.record == newItem.record
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun display(followUp: FollowUp) {
            val dateFollowUp = followUp.dateFollowUp
                    .plusHours(8)
                    .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            val datePlus = ZonedDateTime
                    .now(ZoneOffset.UTC)
                    .plusHours(8)
                    .plusDays(1)
                    .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            val dateNow = ZonedDateTime
                    .now(ZoneOffset.UTC)
                    .plusHours(8)
                    .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))

            val colorValue = when {
                dateFollowUp == dateNow -> {
                    "#FFBF00"
                } // amber
                dateFollowUp == datePlus -> {
                    "#008000"
                } // green
                followUp.dateFollowUp
                        .plusHours(8)
                        .isBefore(ZonedDateTime.now(ZoneOffset.UTC)) -> {
                    "#C82333"
                } // red
                else -> {
                    "#0000FF"
                } // blue
            }

            view.residentNameText.text = followUp.residentName
            view.productFollowUpText.text = followUp.productDescription
            view.followUpDateText.text = followUp.dateFollowUp
                    .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            view.followUpDateText.setTextColor(Color.parseColor(colorValue))
            view.contactText.text = followUp.contact
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_all_follow_up, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                display(entity.record)
                itemView.setOnClickListener { detailClicked(entity.id) }
            }
        }
    }


}