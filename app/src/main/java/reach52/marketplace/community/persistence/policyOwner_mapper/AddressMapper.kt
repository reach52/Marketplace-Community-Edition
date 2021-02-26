package reach52.marketplace.community.persistence.policyOwner_mapper

import reach52.marketplace.community.models.policy_owner.Address
import reach52.marketplace.community.persistence.Marshaler
import reach52.marketplace.community.persistence.Unmarshaler

class AddressMapper : Unmarshaler<Address>, Marshaler<Address> {
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

    override fun marshal(t: Address): Map<String, Any> {
        return mapOf(
            KEY_LINE to t.line,
            KEY_CITY to t.city,
            KEY_COUNTRY to t.country,
            KEY_STATE to t.state,
            KEY_ZIP to t.zip
        )
    }
}