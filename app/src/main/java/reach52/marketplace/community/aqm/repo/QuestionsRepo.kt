package reach52.marketplace.community.aqm.repo

import android.content.Context
import reach52.marketplace.community.R
import reach52.marketplace.community.aqm.entity.Item
import reach52.marketplace.community.aqm.entity.MultiSelectItem
import reach52.marketplace.community.aqm.entity.SingleSelectItem
import reach52.marketplace.community.aqm.entity.TextItem
import reach52.marketplace.community.aqm.entity.TextItem.Companion.ADDRESS
import reach52.marketplace.community.aqm.entity.TextItem.Companion.EMAIL
import reach52.marketplace.community.aqm.entity.TextItem.Companion.NAME
import reach52.marketplace.community.aqm.entity.TextItem.Companion.NUMBER
import reach52.marketplace.community.aqm.entity.TextItem.Companion.PHONE
import reach52.marketplace.community.aqm.entity.TextItem.Companion.TEXT
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.extensions.utils.NetworkManager
import reach52.marketplace.community.extensions.utils.SharedPrefs
import io.reactivex.Single
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.content

object QuestionsRepo {

	fun getQuestions(context: Context, url: String) = Single.create<ArrayList<Item>> {

		if (url == "") {

			val json = """
			
			{
    "questions": [
        {
            "id": "Q1",
            "question": {
                "ENG": "Has attended a reach52 event",
				"KAN": "ರೀಚ್ 52 ಕಾರ್ಯಕ್ರಮಕ್ಕೆ ಹಾಜರಾಗಿದ್ದಾರೆ:",
                "KHM": "ធ្លាប់បានចូលរួមក្នុងសកម្មភាព (ប្រជុំ...) របស់រាជ៥២",
				"HIL": "Naka-attend sang reach52 nga event:"
            },
            "inputType": "boolean"
        },
        {
            "id": "Q2",
            "question": {
                "ENG": "Has used a reach52 chatbot",
				"KAN": "ರೀಚ್ 52 ಚಾಟ್‌ಬಾಟ್ ಅನ್ನು ಬಳಸಿದೆ:",
                "KHM": "តធ្លាប់បានចូលប្រើកម្មវិធី ការផ្ញើរសារជាស្វ័យប្រវត្តិ (chatbot) នៅក្នុងហ្វេសបុករបស់រាជ៥២",
				"HIL": "Nakagamit sang reach52 chatbot :"

            },
            "inputType": "boolean"
        },
        {
            "id": "Q3",
            "question": {
                "ENG": "Is a member of reach52’s Facebook community",
				"KAN": "ರೀಚ್ 52 ರ ಫೇಸ್‌ಬುಕ್ ಸಮುದಾಯದ ಸದಸ್ಯರಾ?:",
                "KHM": "តើអ្នកជាសមាជិកនៅក្នុងហ្វេសបុករាជ៥២មែនទេ?",
				"HIL": "Miyembro ka bala sang reach52 Facebook community? :"

            },
            "inputType": "boolean"
        },
        {
            "id": "Q4",
            "question": {
                "ENG": "Has ordered medicines from reach52 marketplace",
				"KAN": "ರೀಚ್ 52 ಮಾರುಕಟ್ಟೆಯಿಂದ medicineಆದೇಶಿಸಿದೆ:",
                "KHM": "ធ្លាប់បានទិញថ្នាំពីរាជ៥២",
				"HIL": "Naka-order sang bulong halin sa reach52 marketplace :"

            },
            "inputType": "boolean"
        },
		{
		 "id": "Q5",
            "question": {
                "ENG": "Has ordered insurance from reach52 marketplace",
				"KAN": "ರೀಚ್ 52 ಮಾರುಕಟ್ಟೆಯಿಂದ ದ ವಿಮೆಯನ್ನು ಆದೇಶಿಸಿದೆ:",
                "KHM": "ធ្លាប់បានទិញធានារ៉ាប់រងពីរាជ៥២",
				"HIL": "Naka-order sang insurance halin sa reach52 marketplace :"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q6",
            "question": {
                "ENG": "Has ordered diagnostics from reach52 marketplace",
				"KAN": "ರೀಚ್ 52 ಮಾರುಕಟ್ಟೆಯಿಂದ ರೋಗನಿರ್ಣಯ / ರೋಗಲಕ್ಷಣವನ್ನು ಗುರುತಿಸಲು ಆದೇಶಿಸಿದೆ:",
                "KHM": "ធ្លាប់បានទិញ តេស្តពីរាជ៥២",
				"HIL": "Naka-order sang diagnostic halin sa reach52 marketplace :"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q7",
            "question": {
                "ENG": "Has ordered general health products from reach52 marketplace",
				"KAN": "ರೀಚ್ 52 ಮಾರುಕಟ್ಟೆಯಿಂದ ಸಾಮಾನ್ಯ ಆರೋಗ್ಯ ಉತ್ಪನ್ನಗಳನ್ನು ಆದೇಶಿಸಿದೆ:",
                "KHM": "ធ្លាប់បានទិញផលិតផលថែទាំសុខភាពផ្សេងៗពីរាជ៥២",
				"HIL": "Naka-order sang general health products halin sa reach52 marketplace :"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q8",
            "question": {
                "ENG": "Has ordered products more than once",
				"KAN": "ಉತ್ಪನ್ನಗಳನ್ನು ಒಂದಕ್ಕಿಂತ ಹೆಚ್ಚು ಬಾರಿ ಆದೇಶಿಸಿದೆ:",
                "KHM": "ធ្លាប់បានទិញផលិតផលជាច្រើនលើកពីរាជ៥២",
				"HIL": "Naka-order sang mga produkto masobra isa ka beses :"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q9",
            "question": {
                "ENG": "Has not ordered because products are too expensive",
				"KAN": "ಉತ್ಪನ್ನಗಳು ತುಂಬಾ ದುಬಾರಿಯಾಗಿರುವುದರಿಂದ ಆದೇಶಿಸಿಲ್ಲ:",
                "KHM": "មិនទាន់បានទិញផលិតផលអ្វីទាំងអស់ពីរាជ៥២ដោយសាតែតម្លៃថ្លៃពេក",
				"HIL": "Wala nag order tungod mahal ang mga produkto:"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q10",
            "question": {
                "ENG": "Has not ordered because products take too long to deliver",
				"KAN": "ಉತ್ಪನ್ನಗಳನ್ನು ತಲುಪಿಸಲು ಹೆಚ್ಚು ಸಮಯ ತೆಗೆದುಕೊಳ್ಳುವ ಕಾರಣ ಆದೇಶಿಸಿಲ್ಲ:",
                "KHM": "មិនទាន់បានទិញផលិតផលអ្វីទេពីរាជ៥២ដោយសាតែចំណាយពេលយូរពេកទម្រាំទទួលបានផលិតផល",
				"HIL": "Wala nag order tungod kadugay madeliver sang mga produkto :"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q11",
            "question": {
                "ENG": "Has not ordered because reach52 don’t stock the right products",
				"KAN": "ತಲುಪಲು ಸಾಧ್ಯವಿಲ್ಲ ಏಕೆಂದರೆ ತಲುಪಲು 52 ಸರಿಯಾದ ಉತ್ಪನ್ನಗಳನ್ನು ಸಂಗ್ರಹಿಸುವುದಿಲ್ಲ:",
                "KHM": "មិនទាន់បានទិញផលិតផលអ្វីពីរាជ៥២ទេដោយសាតែរាជ៥២មិនមានផលិតផលដែលខ្ញុំចង់បាននៅក្នុងស្តុក",
				"HIL": "Wala nag order tungod ang reach52 hindi makapundo ang instsakto nga mga produkto:"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q12",
            "question": {
                "ENG": "Has not ordered because they can’t locate the MAMs to place an order",
				"KAN": "ಆದೇಶವನ್ನು ನೀಡಲು ಅವರು MAM ಅನ್ನು ಕಂಡುಹಿಡಿಯಲು ಸಾಧ್ಯವಿಲ್ಲದ ಕಾರಣ ಆದೇಶಿಸಿಲ್ಲ:",
                "KHM": "មិនទាន់បានទិញផលិតផលអ្វីពីរាជ៥២ទេដោយសាតែរាជ៥២មិនមានភ្នាក់ងារនៅក្នុងភូមិដែលខ្ញុំរស់នៅ",
				"HIL": "Wala nag order tungod hindi mabal-an kung diin ang MAM para makahatag sang order :"

            },
            "inputType": "boolean"
		},
		{
		 "id": "Q13",
            "question": {
                "ENG": "Has not ordered because they are not aware of reach52 before",
				"KAN": "ಈ ಮೊದಲು ತಲುಪಲು ಅವರಿಗೆ ತಿಳಿದಿಲ್ಲದ ಕಾರಣ ಆದೇಶಿಸಿಲ್ಲ:",
                "KHM": "មិនទាន់បានទិញផលិតផលអ្វីពីរាជ៥២ទេដោយសាតែខ្ញុំមិនដែរធ្លាប់ស្គាល់រាជ៥២ពីមុនមក",
				"HIL": "Wala nag order tungod wala makahibalo parte sa reach52:"

            },
            "inputType": "boolean"
		},
        {
            "id": "Q14",
            "question": {
                "ENG": "What does the catalogue not hold that is wanted",
				"KAN": "ಬಯಸಿದ ಕ್ಯಾಟಲಾಗ್ ಏನು ಹೊಂದಿಲ್ಲ:",
                "KHM": "តើអ្នកយល់ថារាជ៥២នៅខ្វះផលិតផលអ្វីទៀតដែលជាតម្រូវការរបស់អតិថិជន?",
				"HIL": "Ano ang wala sa catalogue nga inyo gusto?:"

            },
            "inputType": "text"
        }
    ]
}
			
			""".trimIndent()
			val questions = parseJson(context, json)
			it.onSuccess(questions)
			return@create
		}

		NetworkManager.makeGETRequest(url, context = context).subscribe(
				{ response ->

					val jsonString = response.json.toString()
					try {
						val questions = parseJson(context, jsonString)
						saveQuestionsToLocal(context, url, jsonString)
						it.onSuccess(questions)
					} catch (e: Exception) {
						it.onError(InvalidQuestionsJsonException())
					}


				},
				{ err ->
					if (err is NetworkManager.NoInternetConnectionException) {

						val savedJson = getLocalQuestions(context, url)
						if (savedJson == null) {
							it.onError(NoQuestionSetFoundException())
							return@subscribe
						}
						try {
							val questions = parseJson(context, savedJson)
							it.onSuccess(questions)
						} catch (e: Exception) {
							deleteQuestionsFromLocal(context, url)
							it.onError(InvalidQuestionsJsonException())
						}

					}
				}
		)

	}

	fun parseJson(context: Context, json: String): ArrayList<Item> {

		val lang = LanguageUtils.getSavedLanguageInISO3()
//		val lang = "en"

		val questions = ArrayList<Item>()

		val questionArray = Json.parseJson(json).jsonObject["questions"]!!.jsonArray

		questionArray.forEach {

			val jo = it.jsonObject
			val id = jo["id"]!!.content
			val text = loadLangObj(jo, "question", lang)
			val optional = if (jo.contains("optional")) jo["optional"]!!.boolean else false
			val inputType = jo["inputType"]!!.content

			when (inputType) {
				NAME, TEXT, NUMBER, PHONE, EMAIL, ADDRESS ->
					questions += TextItem(id, text, optional, inputType)

				"boolean" ->
					questions += SingleSelectItem(id, text, optional, listOf(
							Item.Option("true", context.resources.getString(R.string.yes)),
							Item.Option("false", context.resources.getString(R.string.no))
					))

				"single" -> {

					val options = ArrayList<Item.Option>()
					val oArr = jo["options"]!!.jsonArray
					oArr.forEach { je ->

						val oo = je.jsonObject
						options += Item.Option(
								oo["id"]!!.content,
								loadLangObj(oo, "text", lang)
						)

					}
					questions += SingleSelectItem(id, text, optional, options)

				}
				"multi" -> {

					val options = ArrayList<Item.Option>()
					val oArr = jo["options"]!!.jsonArray
					oArr.forEach { je ->

						val oo = je.jsonObject
						options += Item.Option(
								oo["id"].toString(),
								loadLangObj(oo, "text", lang)
						)

					}
					questions += MultiSelectItem(id, text, optional, options)

				}


			}


		}

		return questions

	}

	private fun loadLangObj(jo: JsonObject, key: String, lang: String, def: String = ""): String {

		val langObj = jo[key]!!.jsonObject

		if (langObj.contains(lang.toLowerCase())) {
			return langObj[lang.toLowerCase()]!!.content
		} else if (langObj.contains(lang.toUpperCase())) {
			return langObj[lang.toUpperCase()]!!.content
		}

		return def

	}

	private fun getLocalQuestions(context: Context, url: String): String? {

		return SharedPrefs.readString(url, null, context)

	}

	private fun saveQuestionsToLocal(context: Context, url: String, json: String) {

		SharedPrefs.write(url, json, context)

	}

	private fun deleteQuestionsFromLocal(context: Context, url: String) {
		SharedPrefs.delete(url, context)
	}


	class NoQuestionSetFoundException : Exception()
	class InvalidQuestionsJsonException : Exception()

}