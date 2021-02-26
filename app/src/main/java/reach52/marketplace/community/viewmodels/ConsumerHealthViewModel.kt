package reach52.marketplace.community.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.None
import arrow.core.Option
import reach52.marketplace.community.models.consumer_health_order.ServiceOrProduct
import reach52.marketplace.community.models.consumer_health_order.ShoppingCart

class ConsumerHealthViewModel: ViewModel() {
    var selectedServiceOrProductID: MutableLiveData<String> = MutableLiveData()
    var selectedCategory: MutableLiveData<ServiceOrProduct> = MutableLiveData()
    var selectedShoppingCart: Option<ShoppingCart> = None
    var selectedProduct: MutableLiveData<ShoppingCart> = MutableLiveData()
}