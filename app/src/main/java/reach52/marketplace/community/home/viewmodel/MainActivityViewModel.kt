package reach52.marketplace.community.home.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import reach52.marketplace.community.DispergoApp
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.replication.SyncManager
import reach52.marketplace.community.resident.repo.ResidentRepo

class MainActivityViewModel : ViewModel() {

	val syncManager = SyncManager(DispergoApp.get())
	val user = LocalUserRepo.getUser(DispergoApp.get())
	val residentCount = MutableLiveData<Int>()

	@SuppressLint("CheckResult")
	fun getResidentCount(context: Context) {

		ResidentRepo.getResidentCount(context).subscribe(
				{
					residentCount.value = it
				},
				{

				}
		)

	}

	fun dispose() {
		syncManager.stopPullReplication()
		syncManager.stopPushReplication()
		syncManager.dispose()
	}


}