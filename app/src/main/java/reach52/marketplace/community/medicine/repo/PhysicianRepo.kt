package reach52.marketplace.community.medicine.repo

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import reach52.marketplace.community.medicine.entity.Physician
import reach52.marketplace.community.medicine.mapper.PhysicianMapper
import reach52.marketplace.community.persistence.DispergoDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object PhysicianRepo {

    fun getAllPhysician(context: Context) = Single.create<ArrayList<Physician>> {
        val view = DispergoDatabase.physicianView(context)
        val query = view.createQuery()

        query.runAsync { rows, _ ->
            val mapper = PhysicianMapper()
            val physicians = ArrayList<Physician>()
            rows.forEach {
                physicians += mapper.unmarshal(it.document.properties)
            }
            it.onSuccess(physicians)
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    //TODO create a test for supplier unmarshal using this function
    fun getAllSuppliersTest() {
        val stringObj = """
            {
              "_id": "xczb1234",
              "_rev": "sjkl1234",
              "licenseNumber": "Doctors license number",
              "physicianName": {
                "givenName": "Jose",
                "familyName": "Rizal",
                "middleName": ""
              },
              "gender": "",
              "medicalSpecialty": "Pediatrician",
              "type": "physician",
              "contact": {
                "email": "forte@gmail.com",
                "phone": "123345",
                "address": []
              },
              "metadata": {
                "version": 2,
                "createdBy": {
                  "userId": "id123",
                  "utcDatetime": "yyyy/mm/dd hh/mm/ss.sss"
                },
                "updatedBy": [
                  {
                    "userId": "id123",
                    "utcDatetime": "yyyy/mm/dd hh/mm/ss.sss"
                  },
                  {
                    "userId": "id123",
                    "utcDatetime": "yyyy/mm/dd hh/mm/ss.sss"
                  }
                ]
              }
            }
        """.trimIndent()

        val physicianMapper = PhysicianMapper()
        val physicianMapTest = ObjectMapper().readValue<MutableMap<String, Any>>(stringObj)
        val physicianUnmarshal = physicianMapper.unmarshal(physicianMapTest)
        val physician: Physician = physicianUnmarshal
        Log.d("ryann", "physician = $physician")
    }
}