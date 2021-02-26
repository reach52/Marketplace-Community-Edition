package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Contact
import reach52.marketplace.community.persistence.Unmarshaler

class ContactMapper : Unmarshaler<Contact> {
    companion object {
        const val KEY_ADDRESS = "address"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "phone"

        private val addressMapper = AddressMapper()
    }

    override fun unmarshal(properties: Map<String, Any>): Contact {
        return Contact(
                address = (properties[KEY_ADDRESS] as List<*>)
                        .filterIsInstance<Map<String, Any>>()
                        .map(addressMapper::unmarshal),
                email = properties[KEY_EMAIL] as String,
                phone = properties[KEY_PHONE] as String
        )
    }

}
