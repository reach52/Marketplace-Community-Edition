package reach52.marketplace.community.resident.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.utils.calculateAgeFromDOB
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.resident.entity.Resident
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.x_logistic_resident_list_item.view.*

class ResidentLogisticAdapter : PagedListAdapter<Entity<Resident>,
        ResidentLogisticAdapter.ViewHolder>(diffCallback()) {
    private val emitter: PublishSubject<Resident> = PublishSubject.create()
    val selectedResident: Observable<Resident> = emitter

    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<Resident>>() {
            override fun areItemsTheSame(a: Entity<Resident>, b: Entity<Resident>):
                    Boolean = a.id == b.id

            override fun areContentsTheSame(a: Entity<Resident>,
                                            b: Entity<Resident>):
                    Boolean = a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.x_logistic_resident_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                display(entity.record)
                itemView.setOnClickListener {
                    emitter.onNext(entity.record)
                }
            }
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(resident: Resident) {
            view.residentNameTextView.text = resident.firstName
            view.residentAgeTextView.text = calculateAgeFromDOB(resident.dob).toString()
            view.residentSexTextView.text = resident.gender

        }
    }
}