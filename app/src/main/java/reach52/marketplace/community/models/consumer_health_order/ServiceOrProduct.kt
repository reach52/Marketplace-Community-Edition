package reach52.marketplace.community.models.consumer_health_order

import org.threeten.bp.ZonedDateTime

 data class ServiceOrProduct (
         val id: String,
         val dateCreated: ZonedDateTime,
         val category: String,
         val products: List<Products>,
         val type: String,
         val createdBy: CreatedBy,
         val updatedBy: List<CreatedBy>,
         val deletedBy: CreatedBy
)