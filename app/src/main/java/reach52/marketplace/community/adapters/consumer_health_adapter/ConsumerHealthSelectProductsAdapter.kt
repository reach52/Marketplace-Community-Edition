package reach52.marketplace.community.adapters.consumer_health_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import kotlinx.android.synthetic.main.search_consumer_health_list_item.view.*


class ConsumerHealthSelectProductsAdapter(private val receiver: (ServiceOrProduct)-> Unit) :
        PagedListAdapter<Entity<ServiceOrProduct>, ConsumerHealthSelectProductsAdapter.ViewHolder>(diffCallback()) {
    companion object{
        fun diffCallback() = object : DiffUtil.ItemCallback<Entity<ServiceOrProduct>>(){
            override fun areItemsTheSame(a: Entity<ServiceOrProduct>, b: Entity<ServiceOrProduct>): Boolean =
                    a.id == b.id

            override fun areContentsTheSame(a: Entity<ServiceOrProduct>, b: Entity<ServiceOrProduct>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_consumer_health_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            getItem(position)?.let { entity ->
                setData(entity.record)
                itemView.setOnClickListener{
                    receiver(entity.record )

                }
            }
        }
    }

    class ViewHolder(private val view: View):
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
      fun setData(products: ServiceOrProduct){
          view.brandName.text = products.products.first().brand
          view.productInfo.text = products.products.first().miscInfo
          view.priceTextView.text = products.products.first().price.toPhilippinesCurrency()
          view.productName.text = products.products.first().name
          view.vatConsumerHealth.text = itemView.context.getString(R.string.vat_exclusive)

          when (!products.products.first().vatInclusive) {
              true -> view.vatConsumerHealth.text = itemView.context.getString(R.string.vat_inclusive)
          }
      }
    }
}

