package reach52.marketplace.community.persistence

import reach52.marketplace.community.models.Company

@Suppress("UNCHECKED_CAST")
class CompanyInsuranceMapper : Unmarshaler<Company>, Marshaler<Company> {

    companion object {
        const val KEY_IDENTIFIER = "identifier"
        const val KEY_NAME = "name"
        const val KEY_TYPE = "type"
        const val KEY_TELECOMMUNICATION = "telecommunication"
        const val KEY_ADDRESS = "address"

        private val addressInsuranceMapper = AddressMapper()
    }

    override fun marshal(t: Company): Map<String, Any> {
        return mapOf(
                KEY_IDENTIFIER to t.identifier,
                KEY_NAME to t.name,
                KEY_TYPE to t.type,
                KEY_TELECOMMUNICATION to t.telecommunication,
                KEY_ADDRESS to t.address.map(addressInsuranceMapper::marshal)
        )
    }

    override fun unmarshal(properties: Map<String, Any>): Company {
        val identifier = properties[KEY_IDENTIFIER] as String
        val name = properties[KEY_NAME] as String
        val type = properties[KEY_TYPE] as String
        val telecommunication = properties[KEY_TELECOMMUNICATION] as String
        val address = (properties[KEY_ADDRESS] as List<*>)
                .filterIsInstance<Map<String, Any>>()
                .map(addressInsuranceMapper::unmarshal)

        return Company(
                identifier = identifier,
                name = name,
                type = type,
                telecommunication = telecommunication,
                address = address
        )
    }
}