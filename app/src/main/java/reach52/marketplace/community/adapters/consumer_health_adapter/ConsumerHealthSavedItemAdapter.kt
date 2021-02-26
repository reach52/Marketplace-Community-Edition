package reach52.marketplace.community.adapters.consumer_health_adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Try
import arrow.core.firstOrNone
import com.jakewharton.rxbinding3.view.longClicks
import com.jakewharton.rxbinding3.widget.textChanges
import reach52.marketplace.community.R
import reach52.marketplace.community.extensions.toPhilippinesCurrency
import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.models.consumer_health_order.OrderLines
import reach52.marketplace.community.models.consumer_health_order.ShoppingCart
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.select_consumer_health_list_item.view.*

class ConsumerHealthSavedItemAdapter :
        RecyclerView.Adapter<ConsumerHealthSavedItemAdapter.ConsumerHealthOrderViewHolder>() {
    companion object{
        private const val TAG = "SelectedProductAdapters"
    }

    private val totalEmitter: PublishSubject<Double> = PublishSubject.create()
    private val disposables = CompositeDisposable()
    private val orderItems: MutableList<SelectedProductViewModel> = mutableListOf()

    val orderTotal: Observable<Double> = totalEmitter.startWith(0.00)

    override fun getItemCount(): Int {
        return orderItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumerHealthOrderViewHolder {
        return ConsumerHealthOrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.select_consumer_health_list_item,
                parent, false))
    }

    override fun onBindViewHolder(holder: ConsumerHealthOrderViewHolder, position: Int) {
        disposables.add(holder.itemView.longClicks { true }.subscribe({
            AlertDialog.Builder(holder.itemView.context).apply {
                setMessage(R.string.remove_item_prompt).setPositiveButton(R.string.remove) { _, _ ->
                    orderItems.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    totalEmitter.onNext(total())
                }.setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }.create().show()
        }, {
            Log.w(TAG, "longClicks", it)
        }))

        disposables.add(holder.itemView.quantityTextInputEditText.textChanges().subscribe({
            Try { it.toString().toInt() }.map {
                orderItems[holder.adapterPosition].quantity = it
                totalEmitter.onNext(total())
            }
        }, {
            Log.w(TAG, "textChanges", it)
        }))

        holder.setData(orderItems[position])
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposables.clear()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun addProducts(products: ShoppingCart){
        orderItems.firstOrNone{ item -> item.products.catalogueID.list.first().productID == products.catalogueID.list.first().productID}.fold({
            orderItems.add(SelectedProductViewModel(products, products.catalogueID.list.first().quantity))
            notifyItemInserted(orderItems.size - 1)
            totalEmitter.onNext(total())
        }, {
            it.quantity++
        })
    }

    fun orderProducts(): OrderLineItems {
        return OrderLineItems.fromList(orderItems.map {
            OrderLines(
                    productID = it.products.catalogueID.list.first().productID,
                    brand = it.products.catalogueID.list.first().brand,
                    miscInfo = it.products.catalogueID.list.first().miscInfo,
                    unitOfMeasure = it.products.catalogueID.list.first().unitOfMeasure,
                    quantity = it.quantity,
                    price = it.products.catalogueID.list.first().price,
                    isVatExclusive = it.products.catalogueID.list.first().isVatExclusive,
                    name = it.products.catalogueID.list.first().name
            )
        })
    }

    private fun total() = orderItems.map {
        it.products.catalogueID.list.first().price * it.quantity
    }.sum()


    class ConsumerHealthOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun setData(orderProductsViewModel: SelectedProductViewModel){
            itemView.apply {
                val cart = orderProductsViewModel.products
                brandTextView.text = cart.catalogueID.list.first().brand
                itemsTextView.text = cart.catalogueID.list.first().name
                infoTextView.text = cart.catalogueID.list.first().miscInfo
                priceTextViews.text = cart.catalogueID.list.first().price.toPhilippinesCurrency()
                quantityTextInputEditText.setText(
                        orderProductsViewModel.quantity.toString()
                )
            }
        }
    }
    data class SelectedProductViewModel(val products: ShoppingCart , var quantity: Int)
}