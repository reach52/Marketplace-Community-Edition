package reach52.marketplace.community.insurance.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.insurance.entity.PolicyOrder
import reach52.marketplace.community.insurance.repo.OrderRepo

class PolicyOrderViewModel : ViewModel() {

	val order = MutableLiveData<PolicyOrder>()

	@SuppressLint("CheckResult")
	fun loadPolicyOrder(context: Context, id: String){

		OrderRepo.getPolicyOrder(context, id).subscribe(
				{
					order.value = it
				},
				{

				}
		)

	}

}