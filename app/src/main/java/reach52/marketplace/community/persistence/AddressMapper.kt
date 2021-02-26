package reach52.marketplace.community.persistence

import arrow.core.Option
import arrow.core.Some
import reach52.marketplace.community.extensions.catOptions
import reach52.marketplace.community.models.insured.Address

class AddressMapper : Marshaler<Address>, Unmarshaler<Address> {
    companion object {
        const val KEY_CITY = "city"
        const val KEY_COUNTRY = "country"
        const val KEY_DISTRICT = "district"
        const val KEY_LINE = "line"
        const val KEY_POSTAL_CODE = "postalCode"
        const val KEY_STATE = "state"
    }

    override fun marshal(t: Address): Map<String, Any> = listOf(
            t.city.map { KEY_CITY to it },
            t.country.map { KEY_COUNTRY to it },
            t.district.map { KEY_DISTRICT to it },
            Some(KEY_LINE to t.line),
            t.postalCode.map { KEY_POSTAL_CODE to it },
            t.state.map { KEY_STATE to it }
    ).catOptions().toMap()

    override fun unmarshal(properties: Map<String, Any>): Address {
        val city = Option.fromNullable(properties[KEY_CITY] as? String)
        val country = Option.fromNullable(properties[KEY_COUNTRY] as? String)
        val district = Option.fromNullable(properties[KEY_DISTRICT] as? String)

        @Suppress("UNCHECKED_CAST")
        val line = properties[KEY_LINE] as List<String>
        val postalCode = Option.fromNullable(properties[KEY_POSTAL_CODE] as? String)
        val state = Option.fromNullable(properties[KEY_STATE] as? String)

        return Address(city = city, country = country, district = district, line = line, postalCode = postalCode, state = state)
    }
}