package reach52.marketplace.community.medicine.repo

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import reach52.marketplace.community.medicine.entity.Discount
import reach52.marketplace.community.medicine.mapper.DiscountMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DiscountRepo {

	val testDiscounts = listOf(
			"""
				{
      "_sync": {
        "cas": "",
        "channels": {
          "discounts": null
        },
        "history": {
          "channels": [
            [
              "discounts"
            ]
          ],
          "parents": [
            -1
          ],
          "revs": [
            "1-b81ab2d619b2e83962f48d738104dda2"
          ]
        },
        "recent_sequences": [
          346
        ],
        "rev": "1-b81ab2d619b2e83962f48d738104dda2",
        "sequence": 346,
        "time_saved": "2020-09-29T05:15:33.591520975Z"
      },
      "amount": 120,
      "conditions": {
        "minAge": 60
      },
      "description": "Discount on Meds for Senior citizens",
      "isPercentage": false,
      "isoCountry": "khr",
      "name": "SENIOR",
      "type": "discounts"
    }
			""".trimIndent(),
			"""
				 {
      "_sync": {
        "cas": "",
        "channels": {
          "discounts": null
        },
        "history": {
          "channels": [
            [
              "discounts"
            ]
          ],
          "parents": [
            -1
          ],
          "revs": [
            "1-da5ed2793b4b5345d000477a75aa695d"
          ]
        },
        "recent_sequences": [
          347
        ],
        "rev": "1-da5ed2793b4b5345d000477a75aa695d",
        "sequence": 347,
        "time_saved": "2020-09-29T05:16:40.276028787Z"
      },
      "amount": 50,
      "conditions": {
        "disability": true
      },
      "description": "Discount on Meds for Disabled citizens",
      "isPercentage": true,
      "isoCountry": "khr",
      "name": "PWD",
      "type": "discounts"
    }
			""".trimIndent()

	)

	fun fetchDiscounts(context: Context) = Single.create<ArrayList<Discount>> {

		val view = DispergoDatabase.discountsView(context)
		val query = view.createQuery()

		query.runAsync { rows, error ->

			val mapper = DiscountMapper()
			val discounts = ArrayList<Discount>()

			rows.forEach { row ->

				try {
					discounts += mapper.unmarshal(row.document.properties)
				} catch (e: Exception) {

				}
			}
			it.onSuccess(discounts)

		}


	}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

	fun getTestDiscount(): Discount {
		val mapper = DiscountMapper()
		val testMap = ObjectMapper().readValue(testDiscounts[0], Map::class.java) as Map<String, Any>
		return mapper.unmarshal(testMap)
	}


}
