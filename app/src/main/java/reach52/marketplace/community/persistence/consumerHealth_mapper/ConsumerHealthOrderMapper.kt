package reach52.marketplace.community.persistence.consumerHealth_mapper

import arrow.core.Option
import reach52.marketplace.community.models.consumer_health_order.ConsumerHealthOrder
import reach52.marketplace.community.models.consumer_health_order.OrderLineItems
import reach52.marketplace.community.persistence.DiscountMapper
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.OrderStatusMapper
import reach52.marketplace.community.persistence.Unmarshaler
import reach52.marketplace.community.persistence.claims_mapper.CustomerMapper
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class ConsumerHealthOrderMapper: Unmarshaler<ConsumerHealthOrder>, Marshaler<ConsumerHealthOrder> {

    companion object{
        const val KEY_ID = "id"
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_CREATED_BY = "createdBy"
        const val KEY_TYPE = "type"
        const val KEY_CUSTOMER = "customer"
        const val KEY_CURRENT_STATUS = "currentStatus"
        const val KEY_PAST_STATUSES = "pastStatuses"
        const val KEY_DISCOUNT_ID_NUMBER = "discountIDNumber"
        const val KEY_DISCOUNT = "discount"
        const val KEY_HCP_NUMBER = "hcpRequestNumber"
        const val KEY_ORDER_LINES = "orderLines"
        const val KEY_DELIVERY_ADDRESS = "deliveryAddress"
        const val KEY_SHOPPING_ID = "shoppingId"
        const val KEY_CONSUMER_ORDERS = "consumerOrders"

        private val orderLinesMapper = OrderLinesMapper()
        private val dateCreatedBy = CreatedByMapper()
        private val customers = CustomerMapper()
        private val orderStatusMapper = OrderStatusMapper()
        private val discountMapper = DiscountMapper()
    }

    override fun marshal(t: ConsumerHealthOrder): Map<String, Any> {
        return mutableMapOf(
                KEY_ID to t.id,
                KEY_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_TYPE to t.type,
                KEY_CREATED_BY to dateCreatedBy.marshal(t.createdBy),
                KEY_CUSTOMER to customers.marshal(t.customer),
                KEY_CURRENT_STATUS to orderStatusMapper.marshal(t.currentStatus),
                KEY_PAST_STATUSES to t.pastStatuses.map(orderStatusMapper::marshal),
                KEY_ORDER_LINES to t.orderLines.list.map(orderLinesMapper::marshal),
                KEY_DELIVERY_ADDRESS to t.deliveryAddress,
                KEY_SHOPPING_ID to t.shoppingId
        ).also { properties ->
            t.discount.map{
                properties[KEY_DISCOUNT] = discountMapper.marshal(it)
            }
            t.discountIDNumber.map{
                properties[KEY_DISCOUNT_ID_NUMBER] = it
            }
            t.hcpRequestNumber.map {
                properties[KEY_HCP_NUMBER] = it
            }
        }
    }

    override fun unmarshal(properties: Map<String, Any>): ConsumerHealthOrder {
        val id = properties[KEY_ID] as String
        val dateCreated = ZonedDateTime.parse(properties[KEY_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val createdByMap = (properties[KEY_CREATED_BY] as Map<String, Any>)
        val createdBy = CreatedByMapper().unmarshal(createdByMap)
        val type = properties[KEY_TYPE] as String
        val customerMap = (properties[KEY_CUSTOMER] as Map<String, Any>)
        val customer = CustomerMapper().unmarshal(customerMap)
        val currentStatus = orderStatusMapper.unmarshal(properties[KEY_CURRENT_STATUS] as Map<String, Any>)
        val pastStatuses = (properties[KEY_PAST_STATUSES] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(orderStatusMapper::unmarshal)
        val discountIDNumber = Option.fromNullable(properties[KEY_DISCOUNT_ID_NUMBER]).map {
            it as String
        }
        val discount =  Option.fromNullable(properties[KEY_DISCOUNT]).map {
            discountMapper.unmarshal(it as Map<String, Any>)
        }
        val hcpRequestNumber =
        Option.fromNullable(properties[KEY_HCP_NUMBER]).map { it as String }
        val orderLines = (properties[KEY_ORDER_LINES] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(orderLinesMapper::unmarshal)
                .let { OrderLineItems.fromList(it) }

        val deliveryAddress = properties[KEY_DELIVERY_ADDRESS] as String
        val shoppingId = properties[KEY_SHOPPING_ID] as String

        return ConsumerHealthOrder(
                id = id,
                dateCreated = dateCreated,
                createdBy = createdBy,
                type = type,
                customer = customer,
                currentStatus = currentStatus,
                pastStatuses = pastStatuses,
                discountIDNumber = discountIDNumber,
                discount = discount,
                hcpRequestNumber = hcpRequestNumber,
                orderLines = orderLines,
                deliveryAddress = deliveryAddress,
                shoppingId = shoppingId
        )
    }
}