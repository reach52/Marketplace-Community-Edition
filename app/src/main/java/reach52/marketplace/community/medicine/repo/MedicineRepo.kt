package reach52.marketplace.community.medicine.repo

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.medicine.mapper.MedicineMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers.io

object MedicineRepo {

    fun getAllMedicines(context: Context, supCode: List<String>) = Single.create<ArrayList<Medicine>>{

        val selectedLang = LanguageUtils.getSavedLanguageInISO3()
        val view = DispergoDatabase.medicineView(context)
        val query = view.createQuery()
      //  query.startKey = "JOSM"
      //  query.endKey = "JOSM"
        query.keys = supCode;
        query.runAsync{ rows, _ ->
            val mapper = MedicineMapper(selectedLang)
            val medicines = ArrayList<Medicine>()
            rows.forEach {
                try {
                    medicines += mapper.unmarshal(it.document.properties)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            it.onSuccess(medicines)
        }
    }.subscribeOn(io()).observeOn(AndroidSchedulers.mainThread())




    fun getAllMedsTest() {
        val stringObj = """
            {
                "_id": "9d7b69d644581edcd0edea46d25b8fcc",
                "_rev": "1-398cab44ec81a0e31153ea8e88ef8231",
                "barCode": "001011",
                "brandName": {
                    "eng": "medication josmef1 text",
                    "khm": "medication josmef1 text in khm"
                },
                "description": {
                    "eng": "product description",
                    "khm": "product description in tagalog"
                },
                "dosage": "40mg",
                "form": {
                    "eng": "sachet",
                    "khm": "sachet in khm"
                },
                "genericName": {
                    "eng": "medication generic josmef1 text",
                    "khm": "medication generic josmef1 text in khm"
                },
                "handlingInstr": {
                    "eng": "handle with care, keep refridgerated",
                    "khm": "handling instructions in khm"
                },
                "information": {
                    "eng": "information text",
                    "khm": "information text in khm"
                },
                "ingredients": {
                    "eng": [
                        "ingredient1",
                        "ingredient2"
                    ],
                    "khm": [
                        "ingredient1 in khm",
                        "ingredient2 in khm"
                    ]
                },
                "isoCountry": "khm",
                "isoCurrency": "khm",
                "manufacturer": "METR",
                "medClass": [
                    "Code1",
                    "Code5"
                ],
                "medCode": "<SNOMED-CT Terms>",
                "metadata": {
                    "createdBy": {
                        "userId": "id123",
                        "utcDatetime": "2020-08-12T03:10:15.976Z"
                    },
                    "updatedBy": [],
                    "version": 1
                },
                "packSize": 18,
                "prescriptionRequired": true,
                "price": 100,
                "prodCategory": [
                    "VIT",
                    "B&J"
                ],
                "promotion": {
                    "eng": "promotion text",
                    "khm": "promotion text in khm"
                },
                "r52CatCode": "PHLPHP",
                "r52CatNo": "AC1231",
                "r52Locale": [
                    "Karnakata"
                ],
                "r52SupplierCode": "JOSM",
                "status": "A",
                "stock": {
                    "qty": 1
                },
                "suppCatNo": "JOSM000001",
                "supplier": {
                    "eng": "Josmef",
                    "khm": "Josmef in khm"
                },
                "tags": [
                    "josmef",
                    "biogesic",
                    "basic",
                    "cambodia"
                ],
                "tax": {
                    "category": "exempt",
                    "isIncluded": false,
                    "percentage": 10,
                    "type": "gst"
                },
                "type": "medication",
                "usdPrice": 50
            }
        """.trimIndent()

        val medsMapper = MedicineMapper("eng")
        val medsMapTest = ObjectMapper().readValue<MutableMap<String, Any>>(stringObj)
        val medsUnmarshal = medsMapper.unmarshal(medsMapTest)
        val medicine: Medicine = medsUnmarshal
        Log.d("ryann", "medicine = $medicine")
    }
}