package reach52.marketplace.community.persistence.consumerHealth_mapper

import reach52.marketplace.community.models.consumer_health_order.CreatedBy
import reach52.marketplace.community.models.consumer_health_order.Products
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class ServiceOrProductMapper: Marshaler<ServiceOrProduct>, Unmarshaler<ServiceOrProduct> {
    companion object{
        const val KEY_ID = "_id"
        const val KEY_DATE_CREATED = "dateCreated"
        const val KEY_CATEGORY = "category"
        const val KEY_PRODUCTS = "products"
        const val KEY_TYPE = "type"
        const val KEY_CREATED_BY = "createdBy"
        const val KEY_UPDATED_BY = "updatedBy"
        const val KEY_DELETED = "deletedBy"
        const val KEY_CONSUMER_PRODUCT = "consumerProduct"

        private val productsMapper = ProductsMapper()
        private val createdByMapper = CreatedByMapper()
    }

    override fun marshal(t: ServiceOrProduct): Map<String, Any> {
        return mapOf(
                KEY_ID to t.id,
                KEY_DATE_CREATED to DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t.dateCreated),
                KEY_CATEGORY to t.category,
                KEY_PRODUCTS to t.products.map(productsMapper::marshal),
                KEY_TYPE to t.type,
                KEY_CREATED_BY to createdByMapper.marshal(t.createdBy),
                KEY_UPDATED_BY to t.updatedBy.map(createdByMapper::marshal),
                KEY_DELETED to createdByMapper.marshal(t.deletedBy)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): ServiceOrProduct {
        val id = properties[KEY_ID] as String
        val dateCreated = ZonedDateTime.parse(properties[ConsumerHealthOrderMapper.KEY_DATE_CREATED] as String, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val category = properties[KEY_CATEGORY] as String

        val products = (properties[KEY_PRODUCTS] as List<Products>)
                .filterIsInstance<Map<String, Any>>()
                .map(productsMapper::unmarshal)

        val type = properties[KEY_TYPE] as String

        val createdByMap = (properties[KEY_CREATED_BY] as Map<String, Any>)
        val createdBy = CreatedByMapper().unmarshal(createdByMap)

        val updatedBy = (properties[KEY_UPDATED_BY] as List<CreatedBy>)
                .filterIsInstance<Map<String, Any>>()
                .map(createdByMapper::unmarshal)
        val deletedByMap = (properties[KEY_DELETED] as Map<String, Any>)
        val deletedBy = CreatedByMapper().unmarshal(deletedByMap)

        return ServiceOrProduct(
                id = id,
                dateCreated = dateCreated,
                category = category,
                products = products,
                type = type,
                createdBy = createdBy,
                updatedBy = updatedBy,
                deletedBy = deletedBy
        )
    }
}