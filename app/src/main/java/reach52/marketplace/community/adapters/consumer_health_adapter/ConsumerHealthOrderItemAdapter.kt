package reach52.marketplace.community.adapters.consumer_health_adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.select_consumer_health_list_item.view.*


class ConsumerHealthOrderItemAdapter :
        RecyclerView.Adapter<ConsumerHealthOrderItemAdapter.ConsumerHealthOrderViewHolder>() {
    companion object{
        private const val TAG = "SelectedProductAdapter"
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

    fun addProducts(products: ServiceOrProduct){
        orderItems.firstOrNone{ item -> item.products.products.first().productID == products.products.first().productID}.fold({
            orderItems.add(SelectedProductViewModel(products, 1))
            notifyItemInserted(orderItems.size - 1)
            totalEmitter.onNext(total())
        }, {
            it.quantity++
        })
    }

    fun orderProducts(): OrderLineItems{
        return OrderLineItems.fromList(orderItems.map {
            OrderLines(
                    productID = it.products.products.first().productID,
                    brand = it.products.products.first().brand,
                    miscInfo = it.products.products.first().miscInfo,
                    unitOfMeasure = it.products.products.first().unitOfMeasure,
                    quantity = it.quantity,
                    price = it.products.products.first().price,
                    isVatExclusive = it.products.products.first().vatInclusive,
                    name = it.products.products.first().name
            )
        })
    }

    private fun total() = orderItems.map {
        it.products.products.first().price * it.quantity
    }.sum()


    class ConsumerHealthOrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
       fun setData(orderProductsViewModel: SelectedProductViewModel){
           itemView.apply {
               val products = orderProductsViewModel.products
               brandTextView.text = products.products.first().brand
               itemsTextView.text = products.products.first().name
               infoTextView.text = products.products.first().miscInfo
               priceTextViews.text = products.products.first().price.toPhilippinesCurrency()
               quantityTextInputEditText.setText(
                       orderProductsViewModel.quantity.toString(),
                               TextView.BufferType.EDITABLE
               )
           }
       }
    }
    data class SelectedProductViewModel(val products: ServiceOrProduct, var quantity: Int)
}