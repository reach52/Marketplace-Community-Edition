package reach52.marketplace.community.persistence.insurer_mapper

import reach52.marketplace.community.models.insurance.Policy
import reach52.marketplace.community.persistence.Unmarshaler

class PolicyMapper: Unmarshaler<Policy> {

    companion object {
        const val KEY_TEXT = "text"
        const val KEY_URL = "url"
    }

    override fun unmarshal(properties: Map<String, Any>): Policy {
        return Policy(
                text = properties[KEY_TEXT] as String,
                url = properties[KEY_URL] as String
        )
    }
}
