package reach52.marketplace.community.adapters.consumer_health_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.Entity
import reach52.marketplace.community.models.consumer_health_order.ShoppingCart
import kotlinx.android.synthetic.main.search_consumer_health_list_item.view.*

class ConsumerHealthSavedAdapter(private val clickedSaved: (ShoppingCart)-> Unit) :
PagedListAdapter<Entity<ShoppingCart>,  ConsumerHealthSavedAdapter.ViewHolder>(diffCallback()){
    companion object{
        fun diffCallback() = object: DiffUtil.ItemCallback<Entity<ShoppingCart>>(){
            override fun areItemsTheSame(a: Entity<ShoppingCart>, b: Entity<ShoppingCart>): Boolean =
                    a.id == b.id

            override fun areContentsTheSame(a: Entity<ShoppingCart>, b: Entity<ShoppingCart>): Boolean =
                    a.record == b.record
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_consumer_health_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply{
            getItem(position)?.let{ entity ->
                setData(entity.record)
                itemView.setOnClickListener{
                    clickedSaved(entity.record)
                }
            }
        }
    }

    class ViewHolder(private val view: View):
            androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        fun setData(cart: ShoppingCart){
            view.brandName.text = cart.catalogueID.list.first().brand
            view.productInfo.text = cart.catalogueID.list.first().miscInfo
            view.priceTextView.text = cart.catalogueID.list.first().price.toPhilippinesCurrency()
            view.productName.text = cart.catalogueID.list.first().name
            view.vatConsumerHealth.text = cart.catalogueID.list.first().quantity.toString()
        }
    }
}