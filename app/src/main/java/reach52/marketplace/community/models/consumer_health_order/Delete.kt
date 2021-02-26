package reach52.marketplace.community.models.consumer_health_order

import com.couchbase.lite.Database
import reach52.marketplace.community.persistence.consumerHealth_mapper.ShoppingCartMapper

class Delete(private val database: Database) {
    private val mapper = ShoppingCartMapper()
    fun get(id: String): ShoppingCart = mapper.unmarshal(database.getDocument(id).properties)

    fun deleteSavedCart(
            shoppingCart: ShoppingCart
    ){
        database.getDocument(shoppingCart.id).delete()
    }
}