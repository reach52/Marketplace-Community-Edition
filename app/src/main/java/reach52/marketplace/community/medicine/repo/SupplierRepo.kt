package reach52.marketplace.community.medicine.repo

// import code below for testing the mapper
import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import reach52.marketplace.community.extensions.utils.LanguageUtils
import reach52.marketplace.community.medicine.entity.Supplier
import reach52.marketplace.community.medicine.mapper.SupplierMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SupplierRepo {
    fun getAllSuppliers(context: Context) = Single.create<ArrayList<Supplier>>{
        val selectedLang = LanguageUtils.getSavedLanguageInISO3()
        val view = DispergoDatabase.suppliersView(context)
        val query = view.createQuery()
        query.runAsync{ rows, _ ->
            val mapper = SupplierMapper(selectedLang)
            val suppliers = ArrayList<Supplier>()
            if(!rows.equals(null)) {
                rows.forEach {
                    try {
                        suppliers += mapper.unmarshal(it.document.properties)
                    } catch (e: Exception) {

                    }
                }
            }

            // code for add select all option in list static
            it.onSuccess(suppliers)
        }

    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    val texstJson = """
        
        {
            jdshjkfabk
        }
        
    """.trimIndent()

    //TODO create a test for supplier unmarshal using this function
    fun getAllSuppliersTest() {
        val stringObj = "{\n" +
                "  \"_id\": \"001c9ad01db9e96e066a8e37640e3c77\",\n" +
                "  \"_rev\": \"1-c4fd9e22c18775048331417588f6524b\",\n" +
                "  \"supplierCode\": \"JOSM\",\n" +
                "  \"isoCountry\": \"phl\",\n" +
                "  \"supplierName\": {\n" +
                "    \"khm\": \"Josmef's Pharmacist in Khm\",\n" +
                "    \"eng\": \"Josmef's Pharmacist\"\n" +
                "  },\n" +
                "  \"deliveryFee\": 24.00,\n" +
                "  \"usdPrice\": 10,\n" +
                "  \"type\": \"medicationSupplier\",\n" +
                "  \"contact\": {\n" +
                "    \"email\": \"forte@gmail.com\",\n" +
                "    \"phone\": \"123345\",\n" +
                "    \"address\": []\n" +
                "  },\n" +
                "  \"metadata\": {\n" +
                "    \"version\": 2,\n" +
                "    \"createdBy\": {\n" +
                "      \"userId\": \"id123\",\n" +
                "      \"utcDatetime\": \"yyyy/mm/dd hh/mm/ss.sss\"\n" +
                "    },\n" +
                "    \"updatedBy\": [\n" +
                "      {\n" +
                "        \"userId\": \"id123\",\n" +
                "        \"utcDatetime\": \"yyyy/mm/dd hh/mm/ss.sss\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"userId\": \"id123\",\n" +
                "        \"utcDatetime\": \"yyyy/mm/dd hh/mm/ss.sss\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}"

        val supMapper = SupplierMapper("eng")
        val supMapTest = ObjectMapper().readValue<MutableMap<String, Any>>(stringObj)
        val supUnmarshal = supMapper.unmarshal(supMapTest)
        val supplier: Supplier = supUnmarshal
        Log.d("ryann", "supplier = $supplier")
    }
}