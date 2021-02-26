package reach52.marketplace.community.medicine.repo

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.extensions.base58String
import reach52.marketplace.community.extensions.toPhotoAttachmentStream
import reach52.marketplace.community.medicine.entity.MedicineOrder
import reach52.marketplace.community.medicine.entity.MedicinePurchase
import reach52.marketplace.community.medicine.mapper.MedicineOrderMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object OrderRepo {

	fun getOrdersForResident(context: Context, residentId: String) = Single.create<ArrayList<MedicineOrder>> {
		val view = DispergoDatabase.orderMedicineView(context)
		val query = view.createQuery()
		query.startKey = residentId
		query.endKey = residentId


		query.runAsync { rows, _ ->
			val mapper = MedicineOrderMapper()
			val orders = ArrayList<MedicineOrder>()

			rows.forEach {
				orders += mapper.unmarshal(it.document.properties)
				Log.d("ryann", "order = ${it.document.properties}")
			}
			orders.sortWith { o1, o2 ->
				o2.date.compareTo(o1.date)
			}
			it.onSuccess(orders)
		}

//		it.onSuccess(getAllOrdersTest())
	}.subscribeOn(io()).observeOn(AndroidSchedulers.mainThread())

	fun createNewOrder(context: Context, purchase: MedicinePurchase) = Completable.create {

		val mapper = MedicineOrderMapper()

		val database = DispergoDatabase.getInstance(context)
		database.createDocument().run {

			val trackingCode = UUID.fromString(id).base58String()
			purchase.trackingCode = trackingCode

			val map = mapper.marshal(purchase)
			putProperties(map)

			val file = File(purchase.prescriptionImage)
			if(file.exists()) {
				BitmapFactory.decodeFile(purchase.prescriptionImage)?.toPhotoAttachmentStream().let {
					createRevision().apply {
						setAttachment("photo", "image/webp", it)
						save()
					}
					it?.close()
				}
			}
			Log.d("taaag", "new meds order doc id: ${id}")
			id

		}

		it.onComplete()


	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun updateOrderStatus(context: Context, order: MedicineOrder, status: String) {

		val user = LocalUserRepo.getUser(context)

		val doc = DispergoDatabase.getInstance(context).getDocument(order.id)
		val mapper = MedicineOrderMapper()
		val rev = doc.update { new ->
			new.properties["currentStatus"] = mapper.getStatus(status, user.displayName, user.id)
			true
		}
		if (rev != null) {
			order.currentStatus = status
		} else {
			throw UpdateFailedException(order)
		}

	}

	//TODO create a test for supplier unmarshal using this function
	fun getAllOrdersTest(): ArrayList<MedicineOrder> {
		val stringObj = """
			{
			    "_id": "8f5217e3-b252-415d-9edb-2f7a95d447bc",
			    "trackingCode": "1234abcd",
			    "currentStatus": {
			        "status": "PENDING",
			        "statusDate": "2020-10-05T04:29:44.742Z",
			        "userDisplayName": "Cambodia MAM",
			        "userId": "5f6da0433d82d80021fa4102"
			    },
			    "delivery": 24,
			    "discount": {
			        "amount": 20,
			        "code": "SENIOR",
			        "description": "Discount on Meds for Senior citizens",
			        "isPercentage": true,
			        "name": "SENIOR"
			    },
			    "discountIdNumber": "12345",
			    "isoCountry": "khm",
			    "isoCurrency": "khm",
			    "items": [
			        {
			            "brandName": "medication josmef1 text",
			            "description": "product description",
			            "dosage": "40mg",
			            "form": "sachet",
			            "genericName": "medication generic josmef1 text",
			            "information": "information text",
			            "ingredients": [],
			            "lineSubTotal": 107.2,
			            "medicationId": "4a2722c93aa532fa79ad3e573f8799a9",
			            "packageSize": "18",
			            "prescriptionRequired": true,
			            "price": 100,
			            "qty": 1,
			            "qtyOriginal": 1,
			            "status": "requested",
			            "statusReason": "",
			            "suppCatNo": "JOSM",
			            "supplier": "Josmef's Pharmacist in ENG",
			            "tax": {
			                "category": "exempt",
			                "included": false,
			                "percentage": 10,
			                "type": "gst"
			            }
			        }
			    ],
			    "metadata": {
			        "createdBy": {
			            "userId": "5f6da0433d82d80021fa4102",
			            "utcDateTime": "2020-10-05T04:29:44.742Z"
			        },
			        "updatedBy": [],
			        "version": 1
			    },
			    "orderTotalPayable": 107.2,
			    "pastStatuses": [],
			    "patientAddress": {
			        "addressLine1": "7th Block, ABC",
			        "city": "Manipur",
			        "country": "Cambodia",
			        "isoCountry": "khm",
			        "zip": "990223"
			    },
			    "patientAge": 50,
			    "patientGender": "Male",
			    "patientName": "world, hello ",
			    "physicianId": "",
			    "physicianLicenseNumber": "License number",
			    "physicianName": ", Physician Name",
			    "prescriptionNumber": "1234",
			    "recipient": "",
			    "residentId": "3e063936-157a-4aae-b464-b9389ca2f35c",
			    "taxPayable": 10,
			    "type": "order"
			}
		""".trimIndent()

		val orders = ArrayList<MedicineOrder>()
		val order = MedicineOrderMapper()
		val orderMapTest = ObjectMapper().readValue<MutableMap<String, Any>>(stringObj)
		val orderUnmarshal = order.unmarshal(orderMapTest)
		val orderMed: MedicineOrder = orderUnmarshal

		orders += orderMed
		Log.d("ryann", "orders = $orders")

		return orders
	}

	class UpdateFailedException(order: MedicineOrder) : Exception("Failed to update order: ${order.id}")

}