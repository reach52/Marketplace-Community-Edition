package reach52.marketplace.community.persistence.medication_mapper

import reach52.marketplace.community.models.medication.Suppliers
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class SuppliersMapper: Marshaler<Suppliers>, Unmarshaler<Suppliers> {
    companion object{
        const val KEY_ID = "_id"
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_SUPPLIER_NAME = "supplierName"
        const val KEY_TYPE = "type"
        const val TYPE_SUPPLIER = "medicationSupplier"
    }

    override fun marshal(t: Suppliers): Map<String, Any> {
        return mapOf(
            KEY_ID to t.id,
            KEY_IDENTIFIER to t.identifier,
            KEY_SUPPLIER_NAME to t.supplierName,
            KEY_TYPE to t.type
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Suppliers {
        return Suppliers(
                id = properties[KEY_ID] as String,
                identifier = properties[KEY_IDENTIFIER] as String,
                supplierName = properties[KEY_SUPPLIER_NAME] as String,
                type = properties[KEY_TYPE] as String
        )
    }
}