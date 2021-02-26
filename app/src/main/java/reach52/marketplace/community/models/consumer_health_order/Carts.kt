package reach52.marketplace.community.models.consumer_health_order

import com.couchbase.lite.Database
import reach52.marketplace.community.models.Resident
import reach52.marketplace.community.models.logistic_resident.LogisticResident
import reach52.marketplace.community.persistence.consumerHealth_mapper.ShoppingCartMapper

class Carts(private val database: Database) {

    private val mapper = ShoppingCartMapper()

    fun residentLogistic(
            resident: LogisticResident,
            orderLine: OrderLineItems
    ): String{
        return database.createDocument().run{
            val save = ShoppingCart(
                    id = id,
                    customerID = resident.residentId,
                    catalogueID = orderLine,
                    type = ShoppingCartMapper.KEY_TYPE
            )
            putProperties(mapper.marshal(save).plus("type" to "shoppingCart"))
            id
        }
    }

    fun residentAccess(
            resident: Resident,
            orderLine: OrderLineItems

            ): String{
        return database.createDocument().run{
            val save = ShoppingCart(
                    id = id,
                    customerID = resident.id ,
                    catalogueID = orderLine,
                    type = ShoppingCartMapper.KEY_TYPE
            )
            putProperties(mapper.marshal(save).plus("type" to "shoppingCart"))
            id
        }
    }

    fun get(id: String): ShoppingCart = mapper.unmarshal(database.getDocument(id).properties)

}