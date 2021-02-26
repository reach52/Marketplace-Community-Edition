package reach52.marketplace.community.insurance.repo

import android.content.Context
import android.util.Log
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.insurance.entity.Insurer
import reach52.marketplace.community.insurance.mapper.InsurerMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object InsurerRepo {

	fun getAllInsurers(context: Context) = Single.create<ArrayList<Insurer>> {

		val selectedLang = LanguageUtils.getSavedLanguageInISO3()

		val view = DispergoDatabase.insurerView(context)
		val query = view.createQuery()
		query.runAsync { rows, error ->
			val mapper = InsurerMapper(selectedLang)
			val insurers = ArrayList<Insurer>()
			rows.forEach {
				insurers += mapper.unmarshal(it.document.properties)
			}
			Log.i("taaag", "got ${insurers.size} insurers")
			it.onSuccess(insurers)
		}

	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}