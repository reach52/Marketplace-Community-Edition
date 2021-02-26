package reach52.marketplace.community.extensions.utils

import android.content.Context
import reach52.marketplace.community.persistence.Api
import io.reactivex.Single

object UpdateManager {

	const val HIGH_PRIORITY = 1
	const val MED_PRIORITY = 2
	const val LOW_PRIORITY = 3

	fun getForceAppUpdateTask(context: Context) = Single.create<Update> {

		val url = Api.forceUpdateUrl

		NetworkManager.makeGETRequest(url, context = context)
				.subscribe(
						{ res ->

							val json = res.json
							val priority = json.getInt("priority")
							val newVersionCode = json.getInt("versionCode")

							it.onSuccess(Update(newVersionCode, priority))
						},
						{ err ->
							it.onError(err)
						}
				)


	}

	class Update(val code: Int, val priority: Int)

}