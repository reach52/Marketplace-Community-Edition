package reach52.marketplace.community.models.consumer_health_order

import arrow.core.Option
import reach52.marketplace.community.models.Discount
import reach52.marketplace.community.models.claims.Customer
import reach52.marketplace.community.models.medication.OrderStatus
import org.threeten.bp.ZonedDateTime

data class ConsumerHealthOrder(
        val id: String,
        val dateCreated: ZonedDateTime,
        val createdBy: CreatedBy,
        val type: String,
        val customer: Customer,
        val currentStatus: OrderStatus,
        val pastStatuses: List<OrderStatus>,
        val discountIDNumber: Option<String>,
        val discount: Option<Discount>,
        val hcpRequestNumber: Option<String>,
        val orderLines: OrderLineItems,
        val deliveryAddress: String,
        val shoppingId: String
){
    // NOTE: Values get resolved in the order they are declared
    val subTotal: Double = orderLines.list.map { it.basePriceTotal }.sum()
    val discountAmount: Double = discount.fold({ 0.00 }, { it.discountAmount(subTotal) })
    val total: Double = discount.fold({ subTotal }, { it.total(subTotal) })

}