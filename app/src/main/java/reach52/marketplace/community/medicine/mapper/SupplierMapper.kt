package reach52.marketplace.community.medicine.mapper

import reach52.marketplace.community.insurance.util.getLangText
import reach52.marketplace.community.medicine.entity.Supplier
import reach52.marketplace.community.persistence.Unmarshaler

@Suppress("UNCHECKED_CAST")
class SupplierMapper(private val selectedLanguage: String = "eng") : Unmarshaler<Supplier> {
    override fun unmarshal(properties: Map<String, Any>): Supplier {
        val id = properties["_id"] as String
        val supplierCode = properties["supplierCode"] as String
        val isoCountry = properties["isoCountry"] as String
        val supplierName = getLangText(properties, "supplierName", selectedLanguage)
        val r52SuppCode =supplierCode;
        val deliveryFee = (properties["deliveryFee"] as Number).toDouble()
        val contact = properties["contact"] as Map<String, String>

        val email = contact["email"] as String
        val phone = contact["phone"] as String
        val status: Int = 0

        return Supplier(
                id,
                supplierCode,
                isoCountry,
                supplierName,
                r52SuppCode,
                deliveryFee,
                email,
                phone,
                status
        )
    }

}