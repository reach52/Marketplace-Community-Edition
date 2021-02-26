package reach52.marketplace.community.medicine.mapper

import reach52.marketplace.community.insurance.util.getDouble
import reach52.marketplace.community.insurance.util.getInt
import reach52.marketplace.community.insurance.util.getLangText
import reach52.marketplace.community.insurance.util.getString
import reach52.marketplace.community.medicine.entity.Medicine
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class MedicineMapper(private val selectedLanguage: String = "eng"): Unmarshaler<Medicine> {
    companion object{
        private val taxMap = TaxMapper()
        private val suppliernameMapper = SupplierNameMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): Medicine {
        val descriptionData = getLangText(properties, "description", selectedLanguage)
        val formData = getLangText(properties, "form", selectedLanguage)
        val dosageData = getString(properties, "dosage")

        val id = properties["_id"] as String
        val barCode = getString(properties, "barCode")
        val description = if(descriptionData == ""){ "NA" } else { descriptionData }
        val dosage = if(dosageData == ""){ "NA" } else { dosageData }
        val form = if(formData == ""){ "NA" } else { formData }
        val handling = getLangText(properties, "handlingInstr", selectedLanguage)
        val information = getLangText(properties, "information", selectedLanguage)
//        val ingredients = getLangList(properties, "ingredients", selectedLanguage)
        val isoCountry = getString(properties, "isoCountry")
        val isoCurrency = getString(properties, "isoCurrency")
        val manufacturer = getString(properties, "manufacturer")
        val medCode = getString(properties, "medCode")
        val brandName = getLangText(properties, "brandName", selectedLanguage)
        val genericName = getLangText(properties, "genericName", selectedLanguage)
        val packSize = getInt(properties, "packSize", 1)
        val packUnit = getString(properties, "packUnit")
        val prescriptionRequired = properties["prescriptionRequired"] as Boolean
        val status = getString(properties, "status")
        val price = getDouble(properties, "price")
        val supplierCode = getString(properties, "r52SupplierCode")
        val tax = taxMap.unmarshal(properties["tax"] as Map<String, Any>)
        val supNameMap = suppliernameMapper.unmarshal(properties["supplier"] as Map<String, Any>)
        return Medicine(
                id,
                barCode,
                description,
                dosage,
                form,
                handling,
                information,
//                ingredients,
                isoCountry,
                isoCurrency,
                manufacturer,
                medCode,
                brandName,
                genericName,
                packSize,
                packUnit,
                prescriptionRequired,
                status,
                price,
                supplierCode,
                tax,
                supNameMap
        )
    }
}

class TaxMapper: Unmarshaler<Medicine.Tax>{
    override fun unmarshal(properties: Map<String, Any>): Medicine.Tax {
        val category = properties["category"] as String
        val isIncluded = properties["isIncluded"] as Boolean
        val percentage = (properties["percentage"] as Number).toDouble()
        val type = properties["type"] as String

        return Medicine.Tax(
                category,
                isIncluded,
                percentage,
                type
        )
    }

}
class SupplierNameMapper: Unmarshaler<Medicine.SupName>{
    override fun unmarshal(properties: Map<String, Any>): Medicine.SupName {
        val eng = properties["eng"] as String
        return Medicine.SupName(eng)
    }


}
