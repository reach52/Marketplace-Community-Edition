package reach52.marketplace.community.persistence.medication_mapper

import reach52.marketplace.community.models.medication.Medication
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class MedicationMapper : Unmarshaler<Medication> {
//    companion object {
//        const val KEY_BAR_CODE = "barCode"
//        const val KEY_DESCRIPTIONS = "descriptions"
//        const val KEY_DOSAGES = "dosages"
//        const val KEY_FORMS = "forms"
//        const val KEY_HANDLING_INSTR = "handlingInstr"
//        const val KEY_COUNTRY = "isoCountry"
//        const val KEY_CURRENCY = "isoCurrency"
//        const val KEY_ID = "_id"
//        const val KEY_INFORMATION = "information"
//        const val KEY_INGREDIENTS = "ingredients"
//        const val KEY_IS_VAT_EXCLUSIVE = "isVatExclusive"
//        const val KEY_MED_CLASS = "medClass"
//        const val KEY_MED_CODE = "medCode"
//        const val KEY_MANUFACTURER = "manufacturer"
//        const val KEY_NAMES = "names"
//        const val KEY_PACKAGE_SIZE = "packageSize"
//        const val KEY_PRICE = "price"
//        const val KEY_PRODUCT_CATEGORIES = "prodCategories"
//        const val KEY_PROMOTION = "promotions"
//        const val KEY_REQ_RX = "reqRx"
//        const val KEY_R52_CODE = "r52CatCode"
//        const val KEY_R52_CAT_NO = "r52CatNo"
//        const val KEY_R52_SUPPLIER_CODE = "r52SupplierCode"
//        const val KEY_SUPP_CODE = "suppCode"
//        const val KEY_STATUS = "status"
//        const val KEY_SUPP_CAT_NUM = "suppCatNo"
//        const val KEY_SUPPLIERS = "suppliers"
//        const val KEY_TYPE = "type"
//        const val TYPE_MEDICATION = "medication"
//        const val KEY_VAT_RATE = "vatRate"
//        const val KEY_USD_PRICE = "USDPrice"
//        const val KEY_CREATED_BY = "createdBy"
//        const val KEY_UPDATED_BY = "updatedBy"
//
//        private val languageMapper = LanguageMapper()
//        private val createdByMapper = CreatedByMapper()
//    }

    companion object {
        const val KEY_DOSAGE = "dosage"
        const val KEY_FORM = "form"
        const val KEY_ID = "_id"
        const val KEY_INGREDIENTS = "ingredients"
        const val KEY_NAME = "name"
        const val KEY_PACKAGE_SIZE = "packageSize"
        const val KEY_PRICE = "price"
        const val KEY_SUPPLIER = "supplier"
        const val TYPE_MEDICATION = "medication"
        const val VAT = "vat"
    }

    override fun unmarshal(properties: Map<String, Any>): Medication = Medication(
//            barCode = Option.fromNullable(properties[KEY_BAR_CODE]).map{
//                it as String
//            },
//            dosages = (properties[KEY_DOSAGES] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            descriptions = (properties[KEY_DESCRIPTIONS] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            forms = (properties[KEY_FORMS] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            handlingInstr = (properties[KEY_HANDLING_INSTR] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            isoCountry = properties[KEY_COUNTRY] as String,
//            isoCurrency = properties[KEY_CURRENCY] as String,
//            id = properties[KEY_ID] as String,
//            information = (properties[KEY_INFORMATION] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            ingredients = (properties[KEY_INGREDIENTS] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            isVatExclusive = properties[KEY_IS_VAT_EXCLUSIVE] as Boolean,
//            manufacturer = Option.fromNullable(properties[KEY_MANUFACTURER]).map{
//                it as String
//            },
//            medClass = Option.fromNullable(properties[KEY_MED_CLASS]).map{
//                it as String
//            },
//            medCode = Option.fromNullable(properties[KEY_MED_CODE]).map {
//                it as String
//            },
//            names = (properties[KEY_NAMES] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            packageSize = (properties[KEY_PACKAGE_SIZE] as Number).toInt(),
//            prodCategories = Option.fromNullable(properties[KEY_PRODUCT_CATEGORIES]).map{
//                it as String
//            },
//            price = (properties[KEY_PRICE] as Number).toDouble(),
//            promotions = Option.fromNullable(properties[KEY_PROMOTION]).map {
//                it as String
//            },
//            reqRx = properties[KEY_REQ_RX] as Boolean,
//            r52Code = properties[KEY_R52_CODE] as String,
//            r52CatNo = properties[KEY_R52_CAT_NO] as String,
//            r52SupplierCode = properties[KEY_R52_SUPPLIER_CODE] as String,
//            suppCode = properties[KEY_SUPP_CODE] as String,
//            status = properties[KEY_STATUS] as String,
//            suppCatNo = properties[KEY_SUPP_CAT_NUM] as String,
//            type = properties[KEY_TYPE] as String,
//            vatRate = (properties[KEY_VAT_RATE] as Number).toDouble(),
//            USDPrice = Option.fromNullable(properties[KEY_USD_PRICE]).map {
//                it as Double
//            },
//            suppliers = (properties[KEY_SUPPLIERS] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            updatedBy = (properties[KEY_UPDATED_BY] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(createdByMapper::unmarshal),
//            createdBy = CreatedByMapper().unmarshal( (properties[KEY_CREATED_BY] as Map<String, Any>))

            dosage = properties[KEY_DOSAGE] as String,
            form = properties[KEY_FORM] as String,
            id = properties[KEY_ID] as String,
            ingredients = (properties[KEY_INGREDIENTS] as List<*>).map { it as String },
            name = properties[KEY_NAME] as String,
            packageSize = (properties[KEY_PACKAGE_SIZE] as Number).toInt(),
            price = (properties[KEY_PRICE] as Number).toDouble(),
            supplier = properties[KEY_SUPPLIER] as String,
            vat = properties[VAT] as Boolean

    )
}