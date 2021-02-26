package reach52.marketplace.community.resident.repo

import android.content.Context
import android.util.Log
import reach52.marketplace.community.auth.repo.LocalUserRepo
import reach52.marketplace.community.persistence.DispergoDatabase
import reach52.marketplace.community.resident.entity.Resident
import reach52.marketplace.community.resident.mapper.ResidentMapper
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

object ResidentRepo {

	val mapper = ResidentMapper()

	fun createNewLogisticResident(context: Context, resident: Resident) {

		resident.createdById = LocalUserRepo.getUser(context).id
		resident.createDateTime = ZonedDateTime.now(ZoneOffset.UTC).toString()

		val database = DispergoDatabase.getInstance(context)
		database.createDocument().run {

			resident.id = id
			putProperties(mapper.marshal(resident))
			Log.d("taaag", "new doc id: ${id}")
			id

		}


	}

	fun updateLogisticResident(context: Context, resident: Resident) = Completable.create {
//		Log.d("irish", "test")

		resident.updates.add(
				Resident.Update(
						LocalUserRepo.getUser(context).id,
						ZonedDateTime.now(ZoneOffset.UTC).toString()
				)
		)

		val doc = DispergoDatabase.getInstance(context).getDocument(resident.id)
		val rev = doc.update { new ->
			new.properties = mapper.marshal(resident)
			true
		}
		if (rev != null) {
			it.onComplete()
		} else {
			it.onError(UpdateFailedException(resident))
		}


	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun fetchResident(context: Context, id: String) = Single.create<Resident> {


		val doc = DispergoDatabase.getInstance(context).getExistingDocument(id)
		if (doc == null) {
			it.onError(NoResidentFoundException(id))
		} else {
			val resident = mapper.unmarshal(doc.properties)
			it.onSuccess(resident)
		}

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun getLogisticResidents(context: Context) = Single.create<ArrayList<Resident>> {

		val view = DispergoDatabase.logisticResidentView(context)
		val query = view.createQuery()
		query.runAsync { rows, error ->

			val residents = ArrayList<Resident>()
			Log.i("taaag", "got ${rows.count} residents")
			for (row in rows) {
				val resident = mapper.unmarshal(row.document.properties)
				residents.add(resident)
			}

			it.onSuccess(residents)


		}
	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun searchLogisticResidents(context: Context, searchString: String) = Single.create<ArrayList<Resident>> {

		val view = DispergoDatabase.logisticResidentView(context)
		val query = view.createQuery()
		query.startKey = searchString.toLowerCase()
		query.endKey = searchString + "\u9999"

		query.runAsync { rows, error ->

			val residents = ArrayList<Resident>()
			Log.i("taaag", "searched and got ${rows.count} residents")
			for (row in rows) {
				val resident = mapper.unmarshal(row.document.properties)
				residents.add(resident)
			}

			it.onSuccess(residents)

		}

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun getResidentCount(context: Context) = Single.create<Int>{

		val view = DispergoDatabase.logisticResidentView(context)
		val query = view.createQuery()
		try {
			it.onSuccess(query.run().count)
		} catch (e: Exception) {
			it.onError(e)
		}

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//
//    fun updateResidentEngagement(context: Context, resident: Resident, answers: Map<String, String>) {
//
//    	// save answers in to resident schema in the database
//
//    }


	fun updateResidentEngagement(context: Context, resident: Resident, url: String, answers: Map<String, String>)  = Completable.create {
//		Log.d("irish", "test")

//		resident.answers = answers
		val doc = DispergoDatabase.getInstance(context).getDocument(resident.id)
		val rev = doc.update { new ->

			val properties = new.properties

			val response = ArrayList<Map<String, Any>>()
			for((k,v) in answers){
				response += mapOf<String, Any>(
						"answer" to listOf(v),
						"questionCode" to k
				)
			}

			// add engagement data to properties before saving
			if(properties.containsKey("engagements")){

				val engagements = properties["engagements"] as ArrayList<Map<String, Any>>

				engagements += mapOf(
						"reviewDate" to ZonedDateTime.now(ZoneOffset.UTC).toString(),
						"questionURL" to url,
						"reviewResponse" to response

				)

				properties["engagements"] = engagements
			} else {
				val engagements = ArrayList<Map<String, Any>>()

				engagements += mapOf(
						"reviewDate" to ZonedDateTime.now(ZoneOffset.UTC).toString(),
						"questionURL" to url,
						"reviewResponse" to response

				)

				properties["engagements"] = engagements
				new.userProperties = properties
				true
			}


			new.userProperties = properties
			true
		}
		if (rev != null) {
			it.onComplete()
		} else {
			it.onError(UpdateFailedException(resident))
		}


	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    class NoResidentFoundException(id: String) : Exception("No resident found for $id")
	class UpdateFailedException(resident: Resident) : Exception("Failed to update resident: ${resident.id}")

}