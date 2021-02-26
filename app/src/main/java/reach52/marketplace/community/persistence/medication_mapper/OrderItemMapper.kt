package reach52.marketplace.community.persistence.medication_mapper

import arrow.core.Option
import reach52.marketplace.community.models.medication.OrderItem
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class OrderItemMapper : Marshaler<OrderItem>, Unmarshaler<OrderItem> {
//    companion object {
//        const val KEY_BAR_CODE = "barCode"
//        const val KEY_COUNTRY = "country"
//        const val KEY_IS_VAT_EXCLUSIVE = "isVatExclusive"
//        const val KEY_LOCAL_MEDICATION_DOSAGE = "localMedicationDosage"
//        const val KEY_LOCAL_MEDICATION_FORM = "localMedicationForm"
//        const val KEY_LOCAL_MEDICATION_INGREDIENTS = "localMedicationIngredients"
//        const val KEY_LOCAL_MEDICATION_NAME = "localMedicationName"
//        const val KEY_LOCAL_MEDICATION_SUPPLIER = "localMedicationSupplier"
//        const val KEY_MED_CODE = "medCode"
//        const val KEY_MEDICATION_DOSAGE = "medicationDosage"
//        const val KEY_MEDICATION_FORM = "medicationForm"
//        const val KEY_MEDICATION_ID = "medicationId"
//        const val KEY_MEDICATION_INGREDIENTS = "medicationIngredients"
//        const val KEY_MEDICATION_NAME = "medicationName"
//        const val KEY_MEDICATION_PACKAGE_SIZE = "medicationPackageSize"
//        const val KEY_PRICE = "price"
//        const val KEY_R52_CAT_NO = "r52CatNo"
//        const val KEY_R52_SUPP_CODE = "r52SuppCode"
//        const val KEY_SUPP_CAT_NO = "suppCatNo"
//        const val KEY_SUPPLIER = "supplier"
//        const val KEY_QUANTITY = "quantity"
//        const val KEY_TYPE = "type"
//        const val KEY_VAT_RATE = "vatRate"
//
//        private val languageMapper = LanguageMapper()
//    }

    companion object {
        const val KEY_MEDICATION_DOSAGE = "medicationDosage"
        const val KEY_MEDICATION_FORM = "medicationForm"
        const val KEY_MEDICATION_ID = "medicationId"
        const val KEY_MEDICATION_INGREDIENTS = "medicationIngredients"
        const val KEY_MEDICATION_NAME = "medicationName"
        const val KEY_MEDICATION_PACKAGE_SIZE = "medicationPackageSize"
        const val KEY_QUANTITY = "quantity"
        const val KEY_PRICE = "price"
        const val KEY_IS_VAT_EXCLUSIVE = "isVatExclusive"
    }

//    override fun marshal(t: OrderItem): Map<String, Any> = mapOf(
//            KEY_BAR_CODE to t.barCode,
//            KEY_COUNTRY to t.country,
//            KEY_IS_VAT_EXCLUSIVE to t.isVatExclusive,
//            KEY_LOCAL_MEDICATION_DOSAGE to t.localMedicationDosage,
//            KEY_LOCAL_MEDICATION_FORM to t.localMedicationForm,
//            KEY_LOCAL_MEDICATION_INGREDIENTS to t.localMedicationIngredients,
//            KEY_LOCAL_MEDICATION_NAME to t.localMedicationName,
//            KEY_LOCAL_MEDICATION_SUPPLIER to t.localMedicationSupplier,
//            KEY_MED_CODE to t.medCode,
//            KEY_MEDICATION_DOSAGE to t.medicationDosage,
//            KEY_MEDICATION_FORM to t.medicationForm,
//            KEY_MEDICATION_ID to t.medicationId,
//            KEY_MEDICATION_INGREDIENTS to t.medicationIngredients.map(languageMapper::marshal),
//            KEY_MEDICATION_NAME to t.medicationName,
//            KEY_MEDICATION_PACKAGE_SIZE to t.medicationPackageSize,
//            KEY_PRICE to t.price,
//            KEY_R52_CAT_NO to t.r52CatNo,
//            KEY_R52_SUPP_CODE to t.r52SuppCode,
//            KEY_SUPP_CAT_NO to t.suppCatNo,
//            KEY_SUPPLIER to t.supplier,
//            KEY_QUANTITY to t.quantity,
//            KEY_TYPE to t.type,
//            KEY_VAT_RATE to t.vatRate
//    )
//
//    override fun unmarshal(properties: Map<String, Any>): OrderItem = OrderItem(
//            barCode = properties[KEY_BAR_CODE] as String,
//            country = properties[KEY_COUNTRY] as String,
//            isVatExclusive = Option.fromNullable(properties[KEY_IS_VAT_EXCLUSIVE]).fold({ false }, { it }) as Boolean,
//            localMedicationDosage = properties[KEY_LOCAL_MEDICATION_DOSAGE] as String,
//            localMedicationForm = properties[KEY_LOCAL_MEDICATION_FORM] as String,
//            localMedicationIngredients = properties[KEY_LOCAL_MEDICATION_INGREDIENTS] as String,
//            localMedicationName = properties[KEY_LOCAL_MEDICATION_NAME] as String,
//            localMedicationSupplier = properties[KEY_LOCAL_MEDICATION_SUPPLIER] as String,
//            medCode = properties[KEY_MED_CODE] as String,
//            medicationDosage = properties[KEY_MEDICATION_DOSAGE] as String,
//            medicationForm = properties[KEY_MEDICATION_FORM] as String,
//            medicationId = properties[KEY_MEDICATION_ID] as String,
//            medicationIngredients = (properties[KEY_MEDICATION_INGREDIENTS] as List<*>)
//                    .filterIsInstance<Map<String, Any>>()
//                    .map(languageMapper::unmarshal),
//            medicationName = properties[KEY_MEDICATION_NAME] as String,
//            medicationPackageSize = (properties[KEY_MEDICATION_PACKAGE_SIZE] as Number).toInt(),
//            price = (properties[KEY_PRICE] as Number).toDouble(),
//            r52CatNo = properties[KEY_R52_CAT_NO] as String,
//            r52SuppCode = properties[KEY_R52_SUPP_CODE] as String,
//            suppCatNo = properties[KEY_SUPP_CAT_NO] as String,
//            supplier = properties[KEY_SUPPLIER] as String,
//            quantity = (properties[KEY_QUANTITY] as Number).toInt(),
//            type = properties[KEY_TYPE] as String,
//            vatRate = (properties[KEY_VAT_RATE] as Number).toDouble()
//    )

    override fun marshal(t: OrderItem): Map<String, Any> = mapOf(
            KEY_MEDICATION_DOSAGE to t.medicationDosage,
            KEY_MEDICATION_FORM to t.medicationForm,
            KEY_MEDICATION_ID to t.medicationId,
            KEY_MEDICATION_INGREDIENTS to t.medicationIngredients,
            KEY_MEDICATION_NAME to t.medicationName,
            KEY_MEDICATION_PACKAGE_SIZE to t.medicationPackageSize,
            KEY_PRICE to t.price,
            KEY_QUANTITY to t.quantity,
            KEY_IS_VAT_EXCLUSIVE to t.isVatExclusive
    )

    override fun unmarshal(properties: Map<String, Any>): OrderItem = OrderItem(
            properties[KEY_MEDICATION_DOSAGE] as String,
            properties[KEY_MEDICATION_FORM] as String,
            properties[KEY_MEDICATION_ID] as String,
            (properties[KEY_MEDICATION_INGREDIENTS] as List<*>).map { it as String },
            properties[KEY_MEDICATION_NAME] as String,
            (properties[KEY_MEDICATION_PACKAGE_SIZE] as Number).toInt(),
            (properties[KEY_PRICE] as Number).toDouble(),
            (properties[KEY_QUANTITY] as Number).toInt(),
            Option.fromNullable(properties[KEY_IS_VAT_EXCLUSIVE]).fold({ false }, { it }) as Boolean
    )
}