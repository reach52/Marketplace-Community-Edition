package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Address
import reach52.marketplace.community.persistence.Unmarshaler

class AddressMapper : Unmarshaler<Address>{
    companion object {
        const val KEY_LINE = "line"
        const val KEY_CITY = "city"
        const val KEY_COUNTRY = "country"
        const val KEY_STATE = "state"
        const val KEY_ZIP = "zip"
    }

    override fun unmarshal(properties: Map<String, Any>): Address {
        return Address(
                line = (properties[KEY_LINE] as List<*>).map { it as String },
                city = properties[KEY_CITY] as String,
                country = properties[KEY_COUNTRY] as String,
                state = properties[KEY_STATE] as String,
                zip = properties[KEY_ZIP] as String
        )
    }
}
