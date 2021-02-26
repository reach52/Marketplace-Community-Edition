package reach52.marketplace.community.insurance.repo

import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.insurance.entity.PolicyOrder
import reach52.marketplace.community.insurance.entity.PolicyPurchase
import reach52.marketplace.community.insurance.mapper.PolicyOrderMapper
import reach52.marketplace.community.persistence.Api
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object OrderRepo {

	fun uploadIDImage(image: File, context: Context) = Single.create<String> {

//		uncomment this to mute api
//		it.onSuccess("https://r52-photos.ap-south-1.linodeobjects.com/public/default/2020-09-13T14%3A34%3A48.404Z-Unknown.png")
//		return@create

		NetworkManager.makeMultipartRequest(
				NetworkManager.POST,
				Api.imageUploadUrl,
				null,
				files = mapOf("file" to image),
				context = context
		).subscribe({ response ->

			if (response.isSuccessfull) {
				val content = response.json.getJSONObject("content")
				val filepath = content.getString("filePath")
				it.onSuccess(filepath)
			} else {
				it.onError(IDUploadFailException())
			}

		}, { error ->

			if (error.message == "Image size is not acceptable") {
				it.onError(ImageTooLargeException())
			} else {
				it.onError(error)
			}

		})

	}.doOnError {

		FirebaseCrashlytics.getInstance().recordException(it)

	}

	fun createNewPolicyOrder(context: Context, purchase: PolicyPurchase) = Completable.create {

		purchase.orderStatus = PolicyPurchase.OrderStatus().apply {
			enteredOn = ZonedDateTime.now(ZoneOffset.UTC)
		}
		purchase.docHistory = PolicyPurchase.DocHistory(
				ZonedDateTime.now(ZoneOffset.UTC),
				purchase.insurer.id,
				ArrayList()
		)

		val mapper = PolicyOrderMapper()
		val database = DispergoDatabase.getInstance(context)
		database.createDocument().run {

			val trackingCode = UUID.fromString(id).base58String()
			purchase.trackingCode = trackingCode
			putProperties(mapper.marshal(purchase))
			Log.d("taaag", "new order doc id: ${id}")
			id

		}

		it.onComplete()

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun getPolicyOrders(context: Context, residentId: String) = Single.create<ArrayList<PolicyOrder>> {

		val view = DispergoDatabase.policyOrdersView(context)
		val query = view.createQuery()
		query.startKey = residentId
		query.endKey = residentId
		val orders = ArrayList<PolicyOrder>()
		val mapper = PolicyOrderMapper()
		query.runAsync { rows, error ->
			rows.forEach {

				try {
					orders += mapper.unmarshal(it.document.properties)
				} catch (e: Exception) {

				}

			}
			orders.sortWith(Comparator { o1, o2 ->
				o2.createdOn.compareTo(o1.createdOn)
			})
			Log.i("taaag", "got ${orders.size} policy orders for id: $residentId")
			it.onSuccess(orders)
		}

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	// NOTE: this function is for testing only
	fun deleteOrder(context: Context, order: PolicyOrder) {

		DispergoDatabase.getInstance(context).getDocument(order.id).delete()

	}

	fun getPolicyOrder(context: Context, id: String) = Single.create<PolicyOrder> {

		val doc = DispergoDatabase.getInstance(context).getDocument(id)
		val order = PolicyOrderMapper().unmarshal(doc.properties)
		it.onSuccess(order)

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	class ImageTooLargeException : Exception()

	class IDUploadFailException : Exception()

}