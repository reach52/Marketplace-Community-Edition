package reach52.marketplace.community.insurance.repo

import android.content.Context
import reach52.marketplace.community.insurance.entity.InsuranceProduct
import reach52.marketplace.community.insurance.mapper.InsuranceProductMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Single


object InsuranceProductRepo {

	val testJson = """
		
		{
			"_id":"293029",
			
		}
		
	""".trimIndent()

	fun getInsuranceProductsFromInsurer(context: Context, insurerCode: String, selectedLang: String = "eng") = Single.create<ArrayList<InsuranceProduct>> {

		val view = DispergoDatabase.insuranceProductsView(context)
		val query = view.createQuery()
		query.startKey = insurerCode
		query.endKey = insurerCode
		val rows = query.run()

		val products = ArrayList<InsuranceProduct>()
		val mapper = InsuranceProductMapper(selectedLang)

//		val testMap = ObjectMapper().readValue(testJson, Map::class.java) as Map<String, Any>
//		products += mapper.unmarshal(testMap)

		for (row in rows) {
			val product = mapper.unmarshal(row.document.properties)
			products += product
		}
		it.onSuccess(products)

	}

}