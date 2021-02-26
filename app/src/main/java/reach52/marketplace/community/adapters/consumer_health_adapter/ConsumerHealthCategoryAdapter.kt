package reach52.marketplace.community.adapters.consumer_health_adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import reach52.marketplace.community.R
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import kotlinx.android.synthetic.main.category_consumer_health.view.*


class ConsumerHealthCategoryAdapter(private val onClicked: (String, ServiceOrProduct) ->Unit):
        PagedListAdapter<Entity<ServiceOrProduct>, ConsumerHealthCategoryAdapter.ViewHolder>(
                diffCallback()
        ){

    companion object {
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<ServiceOrProduct>>() {
            override fun areItemsTheSame(a: Entity<ServiceOrProduct>, b: Entity<ServiceOrProduct>): Boolean = a.id == b.id
            override fun areContentsTheSame(a: Entity<ServiceOrProduct>, b: Entity<ServiceOrProduct>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.category_consumer_health, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.apply {
            getItem(position)?.let { entity ->
                display(entity.record)
                itemView.setOnClickListener { onClicked(entity.id, entity.record) }
            }
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun display(productCategory: ServiceOrProduct) {
            view.categoryName.text = productCategory.category
            Log.d("aron", productCategory.category)
        }
    }
}
